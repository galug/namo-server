package com.example.namo2.domain.schedule;

import com.example.namo2.domain.category.dao.repository.CategoryRepository;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.schedule.domain.Alarm;
import com.example.namo2.domain.schedule.domain.Period;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleAndUserRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.application.impl.MoimService;
import com.example.namo2.domain.schedule.dto.DiaryDto;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.schedule.domain.Image;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.schedule.dto.GetDiaryRes;
import com.example.namo2.domain.schedule.dto.GetScheduleRes;
import com.example.namo2.domain.schedule.dto.OnlyDiaryDto;
import com.example.namo2.domain.schedule.dto.PostScheduleReq;
import com.example.namo2.domain.schedule.dto.ScheduleIdRes;
import com.example.namo2.domain.schedule.dto.SliceDiaryDto;
import com.example.namo2.domain.user.UserRepository;
import com.example.namo2.global.utils.Converter;
import com.example.namo2.global.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_CATEGORY_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_USER_FAILURE;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userDao;
    private final AlarmRepository alarmRepository;
    private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;
    private final MoimScheduleRepository moimScheduleRepository;
    private final MoimService moimService;
    private final FileUtils fileUtils;
    private final Converter converter;

    @Transactional(readOnly = false)
    public ScheduleIdRes createSchedule(PostScheduleReq postScheduleReq, Long userId) throws BaseException {
        User findUser = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        Category findCategory = categoryRepository.findById(postScheduleReq.getCategoryId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        Period period = Period.builder()
                .startDate(postScheduleReq.getStartDate())
                .endDate(postScheduleReq.getEndDate())
                .dayInterval(postScheduleReq.getInterval())
                .build();
        Schedule schedule = Schedule.builder()
                .name(postScheduleReq.getName())
                .period(period)
                .x(postScheduleReq.getX())
                .y(postScheduleReq.getY())
                .locationName(postScheduleReq.getLocationName())
                .user(findUser)
                .category(findCategory).build();

        for (Integer alarm : postScheduleReq.getAlarmDate()) {
            Alarm alarmEntity = Alarm.builder()
                    .alarmDate(alarm)
                    .schedule(schedule)
                    .build();
            schedule.addAlarm(alarmEntity);
        }
        Schedule saveSchedule = scheduleRepository.save(schedule);
        return new ScheduleIdRes(saveSchedule.getId());
    }

    public List<GetScheduleRes> findUsersSchedule(long userId, List<LocalDateTime> localDateTimes) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        return scheduleRepository.findSchedulesByUserId(user, localDateTimes.get(0), localDateTimes.get(1));
    }

    public List<GetScheduleRes> findUsersMoimSchedule(long userId, List<LocalDateTime> localDateTimes) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        return scheduleRepository.findMoimSchedulesByUserId(user, localDateTimes.get(0), localDateTimes.get(1));
    }

    @Transactional(readOnly = false)
    public ScheduleIdRes updateSchedule(Long scheduleId, PostScheduleReq postScheduleReq) throws BaseException {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        Category category = categoryRepository.findById(postScheduleReq.getCategoryId()).orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));

        Period period = Period.builder()
                .startDate(postScheduleReq.getStartDate())
                .endDate(postScheduleReq.getEndDate())
                .dayInterval(postScheduleReq.getInterval())
                .build();

        schedule.clearAlarm();
        for (Integer alarmDate : postScheduleReq.getAlarmDate()) {
            Alarm alarmEntity = Alarm.builder()
                    .alarmDate(alarmDate)
                    .schedule(schedule)
                    .build();
            schedule.addAlarm(alarmEntity);
        }

        schedule.updateSchedule(
                postScheduleReq.getName(),
                period,
                category,
                postScheduleReq.getX(),
                postScheduleReq.getY(),
                postScheduleReq.getLocationName());
        return new ScheduleIdRes(schedule.getId());
    }

    @Transactional(readOnly = false)
    public void deleteSchedule(Long scheduleId, Integer kind, Long userId) throws BaseException {
        if (kind == 0) {
            Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
            scheduleRepository.delete(schedule);
            return;
        }
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        MoimSchedule moimSchedule = moimScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserRepository.findMoimScheduleAndUserByMoimScheduleAndUser(moimSchedule, user)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE));
        // 마지막 사람이면 모임 스케줄 삭제
        if (moimSchedule.isLastScheduleMember()) {
            moimService.deleteSchedule(scheduleId);
            return;
        }
        moimScheduleAndUserRepository.delete(moimScheduleAndUser);
    }

    @Transactional(readOnly = false)
    public ScheduleIdRes createDiary(Long scheduleId, String content, List<MultipartFile> imgs) throws BaseException {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        schedule.updateDiaryContents(content);
        if (imgs != null) {
            List<String> urls = fileUtils.uploadImages(imgs);
            for (String url : urls) {
                Image image = Image.builder().schedule(schedule).imgUrl(url).build();
                imageRepository.save(image);
            }
        }
        return new ScheduleIdRes(schedule.getId());
    }

    public SliceDiaryDto<DiaryDto> findMonthDiary(Long userId, List<LocalDateTime> localDateTimes, Pageable pageable) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        return scheduleRepository.findScheduleDiaryByMonthDto(user, localDateTimes.get(0), localDateTimes.get(1), pageable);
    }

    public List<OnlyDiaryDto> findAllDiary(Long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        return scheduleRepository.findAllScheduleDiary(user);
    }

    public GetDiaryRes findDiary(Long scheduleId) {
        Schedule schedule = scheduleRepository.findOneScheduleAndImages(scheduleId);

        schedule.existDairy();

        List<String> imgUrls = schedule.getImages().stream()
                .map(image -> image.getImgUrl())
                .collect(Collectors.toList());
        return new GetDiaryRes(schedule.getContents(), imgUrls);
    }

    @Transactional(readOnly = false)
    public void deleteDiary(Long scheduleId) throws BaseException {
        Schedule schedule = scheduleRepository.findOneScheduleAndImages(scheduleId);
        schedule.deleteDiary();
        List<String> urls = schedule.getImages().stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());
        imageRepository.deleteDiaryImages(schedule);
        fileUtils.deleteImages(urls);
    }

    public List<GetScheduleRes> findUsersALLSchedule(Long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        return scheduleRepository.findSchedulesByUserId(user, null, null);
    }

    public List<GetScheduleRes> findMoimALLSchedule(Long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        return scheduleRepository.findMoimSchedulesByUserId(user, null, null);
    }

    public List<Schedule> getSchedules(List<User> users) {
        return scheduleRepository.findSchedulesByUsers(users);
    }
}

package com.example.namo2.schedule;

import com.example.namo2.category.CategoryDao;
import com.example.namo2.config.exception.BaseException;
import com.example.namo2.entity.Period;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.entity.Category;
import com.example.namo2.entity.Image;
import com.example.namo2.entity.Schedule;
import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.GetDiaryRes;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.ScheduleIdRes;
import com.example.namo2.user.UserDao;
import com.example.namo2.utils.Converter;
import com.example.namo2.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_CATEGORY_FAILURE;
import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE;
import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_USER_FAILURE;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {
    private final CategoryDao categoryDao;
    private final ImageDao imageDao;
    private final ScheduleDao scheduleDao;
    private final UserDao userDao;
    private final FileUtils fileUtils;
    private final Converter converter;

    @Transactional(readOnly = false)
    public ScheduleIdRes createSchedule(PostScheduleReq postScheduleReq, Long userId) throws BaseException {
        User findUser = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        Category findCategory = categoryDao.findById(postScheduleReq.getCategoryId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        Schedule schedule = Schedule.builder()
                .name(postScheduleReq.getName())
                .period(new Period(postScheduleReq.getStartDate(), postScheduleReq.getEndDate(), postScheduleReq.getAlarmDate()))
                .location(converter.convertPoint(postScheduleReq.getX(), postScheduleReq.getY()))
                .user(findUser)
                .category(findCategory).build();

        Schedule saveSchedule = scheduleDao.save(schedule);
        return new ScheduleIdRes(saveSchedule.getId());
    }

    public List<GetScheduleRes> findUsersSchedule(long userId, List<LocalDateTime> localDateTimes) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        return scheduleDao.findSchedulesByUserId(user, localDateTimes.get(0), localDateTimes.get(1));
    }

    @Transactional(readOnly = false)
    public ScheduleIdRes updateSchedule(Long scheduleId, PostScheduleReq scheduleReq) throws BaseException {
        Schedule schedule = scheduleDao.findById(scheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        Category category = categoryDao.findById(scheduleReq.getCategoryId()).orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));

        schedule.updateSchedule(
                scheduleReq.getName(),
                new Period(scheduleReq.getStartDate(), scheduleReq.getEndDate(), scheduleReq.getAlarmDate()),
                converter.convertPoint(scheduleReq.getX(), scheduleReq.getY()),
                category);
        return new ScheduleIdRes(schedule.getId());
    }

    @Transactional(readOnly = false)
    public void deleteSchedule(Long scheduleId) throws BaseException {
        Schedule schedule = scheduleDao.findById(scheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        scheduleDao.delete(schedule);
    }

    @Transactional(readOnly = false)
    public ScheduleIdRes createDiary(Long scheduleId, String content, List<MultipartFile> imgs) throws BaseException {
        Schedule schedule = scheduleDao.findById(scheduleId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        schedule.updateDiaryContents(content);
        if (imgs != null) {
            List<String> urls = fileUtils.uploadImages(imgs);
            for (String url : urls) {
                Image image = Image.builder().schedule(schedule).imgUrl(url).build();
                imageDao.save(image);
            }
        }
        return new ScheduleIdRes(schedule.getId());
    }

    public List<DiaryDto> findMonthDiary(Long userId, List<LocalDateTime> localDateTimes) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        List<Schedule> schedules = scheduleDao.findScheduleDiaryByMonthDtoWithNotPaging(user, localDateTimes.get(0), localDateTimes.get(1));
        List<DiaryDto> diaries = new ArrayList<>();
        for (Schedule schedule : schedules) {
            List<String> images = schedule.getImages().stream().map(Image::getImgUrl)
                    .collect(Collectors.toList());
            diaries.add(new DiaryDto(schedule.getId(), schedule.getName()
                    , schedule.getPeriod().getStartDate(), schedule.getContents(), images));
        }
        return diaries;
    }

    public GetDiaryRes findDiary(Long scheduleId) {
        Schedule schedule = scheduleDao.findScheduleAndImages(scheduleId);

        schedule.existDairy();

        List<String> imgUrls = schedule.getImages().stream()
                .map(image -> image.getImgUrl())
                .collect(Collectors.toList());
        return new GetDiaryRes(schedule.getContents(), imgUrls);
    }

    @Transactional(readOnly = false)
    public void deleteDiary(Long scheduleId) throws BaseException {
        Schedule schedule = scheduleDao.findScheduleAndImages(scheduleId);
        schedule.deleteDiary();
        List<String> urls = schedule.getImages().stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());
        imageDao.deleteDiaryImages(schedule);
        fileUtils.deleteImages(urls);
    }
}

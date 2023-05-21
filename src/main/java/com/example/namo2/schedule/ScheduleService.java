package com.example.namo2.schedule;

import com.example.namo2.category.CategoryDao;
import com.example.namo2.config.exception.BaseException;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.entity.Category;
import com.example.namo2.entity.Image;
import com.example.namo2.entity.Schedule;
import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.ScheduleIdRes;
import com.example.namo2.schedule.dto.ScheduleDto;
import com.example.namo2.user.UserDao;
import com.example.namo2.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.namo2.config.response.BaseResponseStatus.JPA_FAILURE;
import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_CATEGORY_FAILURE;
import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE;
import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_USER_FAILURE;


@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final CategoryDao categoryDao;
    private final ImageDao imageDao;
    private final ScheduleDao scheduleDao;
    private final UserDao userDao;
    private final FileUtils fileUtils;

    public ScheduleIdRes createSchedule(ScheduleDto scheduleDto, Long userId) throws BaseException {
        User findUser = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        Category findCategory = categoryDao.findById(scheduleDto.getCategoryId()).orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));

        try {
            Schedule schedule = Schedule.builder()
                    .name(scheduleDto.getName())
                    .period(scheduleDto.getPeriod())
                    .location(scheduleDto.getPoint())
                    .user(findUser)
                    .category(findCategory).build();
            Schedule saveSchedule = scheduleDao.save(schedule);
            return new ScheduleIdRes(saveSchedule.getId());
        } catch (Exception exception) {
            throw new BaseException(JPA_FAILURE);
        }
    }

    @Transactional(readOnly = true)
    public List<GetScheduleRes> findUsersSchedule(long userId, List<LocalDateTime> localDateTimes) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        try {
            return scheduleDao.findSchedulesByUserId(user, localDateTimes.get(0), localDateTimes.get(1));
        } catch (Exception exception) {
            throw new BaseException(JPA_FAILURE);
        }
    }

    public ScheduleIdRes updateSchedule(Long scheduleId, ScheduleDto scheduleDto) throws BaseException {
        Schedule schedule = scheduleDao.findById(scheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        Category category = categoryDao.findById(scheduleDto.getCategoryId()).orElseThrow();

        try {
            schedule.updateSchedule(scheduleDto.getName(), scheduleDto.getPeriod(), scheduleDto.getPoint(), category);
            return new ScheduleIdRes(schedule.getId());
        } catch (Exception exception) {
            throw new BaseException(JPA_FAILURE);
        }
    }

    public void deleteSchedule(Long scheduleId) throws BaseException {
        Schedule schedule = scheduleDao.findById(scheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));

        try {
            scheduleDao.delete(schedule);
        } catch (Exception exception) {
            throw new BaseException(JPA_FAILURE);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ScheduleIdRes createDiary(Long scheduleId, String content) throws BaseException {
        Schedule schedule = scheduleDao.findById(scheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        try {
            schedule.updateDiaryContents(content);
            return new ScheduleIdRes(schedule.getId());
        } catch (Exception e) {
            throw new BaseException(JPA_FAILURE);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ScheduleIdRes createDiary(Long scheduleId, String content, List<MultipartFile> imgs) throws BaseException {
        Schedule schedule = scheduleDao.findById(scheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        schedule.updateDiaryContents(content);
        try {
            List<String> urls = fileUtils.uploadImages(imgs);
            for (String url : urls) {
                Image image = Image.builder().schedule(schedule).imgUrl(url).build();
                imageDao.save(image);
            }
            return new ScheduleIdRes(schedule.getId());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(JPA_FAILURE);
        }
    }

    @Transactional(readOnly = true)
    public List<DiaryDto> findMonthDiary(Long userId, List<LocalDateTime> localDateTimes) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        try {
            List<Schedule> schedules = scheduleDao.findScheduleDiaryByMonthDtoWithNotPaging(user, localDateTimes.get(0), localDateTimes.get(1));
            List<DiaryDto> diaries = new ArrayList<>();
            for (Schedule schedule : schedules) {
                List<String> images = schedule.getImages().stream().map(Image::getImgUrl)
                        .collect(Collectors.toList());
                diaries.add(new DiaryDto(schedule.getId(), schedule.getName()
                        , schedule.getPeriod().getStartDate(), schedule.getContents(), images));
            }
            return diaries;
        } catch (Exception e) {
            throw new BaseException(JPA_FAILURE);
        }
    }

    public void deleteDiary(Long scheduleId) throws BaseException {
        Schedule schedule = scheduleDao.findScheduleAndImages(scheduleId);
        try {
            schedule.deleteDiary();
            List<String> urls = schedule.getImages().stream()
                    .map(Image::getImgUrl)
                    .collect(Collectors.toList());
            imageDao.deleteDiaryImages(schedule);
            fileUtils.deleteImages(urls);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(JPA_FAILURE);
        }
    }
}

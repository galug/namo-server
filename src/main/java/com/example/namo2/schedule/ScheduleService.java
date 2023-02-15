package com.example.namo2.schedule;

import com.example.namo2.category.CategoryDao;
import com.example.namo2.config.BaseException;
import com.example.namo2.config.BaseResponseStatus;
import com.example.namo2.entity.Category;
import com.example.namo2.entity.Period;
import com.example.namo2.entity.Schedule;
import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.ScheduleIdRes;
import com.example.namo2.schedule.dto.ScheduleDto;
import com.example.namo2.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static com.example.namo2.config.BaseResponseStatus.JPA_FAILURE;
import static com.example.namo2.config.BaseResponseStatus.NOT_FOUND_CATEGORY_FAILURE;
import static com.example.namo2.config.BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE;
import static com.example.namo2.config.BaseResponseStatus.NOT_FOUND_USER_FAILURE;


@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final UserDao userDao;
    private final CategoryDao categoryDao;

    public ScheduleIdRes createSchedule(ScheduleDto scheduleDto, Long userId) throws BaseException {
        User findUser = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        Category findCategory = categoryDao.findById(scheduleDto.getCategoryId()).orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        try {
            Schedule schedule = Schedule.builder()
                    .name(scheduleDto.getName())
                    .period(scheduleDto.getPeriod())
                    .point(scheduleDto.getPoint())
                    .user(findUser)
                    .category(findCategory).build();
            Schedule saveSchedule = scheduleDao.save(schedule);
            return new ScheduleIdRes(saveSchedule.getId());
        } catch (Exception exception) {
            throw new BaseException(JPA_FAILURE);
        }
    }

    @Transactional(readOnly = true)
    public List<GetScheduleRes> findUsersSchedule(long userId) throws BaseException{
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        try {
            return scheduleDao.findSchedulesByUserId(user);
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
}

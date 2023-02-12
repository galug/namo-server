package com.example.namo2.schedule;

import com.example.namo2.category.CategoryDao;
import com.example.namo2.config.BaseException;
import com.example.namo2.entity.Category;
import com.example.namo2.entity.Schedule;
import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.PostScheduleRes;
import com.example.namo2.schedule.dto.ScheduleDto;
import com.example.namo2.user.UserDao;
import com.example.namo2.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final UserDao userDao;
    private final CategoryDao categoryDao;

    public PostScheduleRes createSchedule(ScheduleDto scheduleDto, Long userId, Long categoryId) throws BaseException {
        User findUser = userDao.findById(userId).orElseThrow(InvalidParameterException::new);
        Category findCategory = categoryDao.findById(categoryId).orElseThrow(InvalidParameterException::new);

        Schedule schedule = Schedule.builder()
                .name(scheduleDto.getName())
                .period(scheduleDto.getPeriod())
                .point(scheduleDto.getPoint())
                .user(findUser)
                .category(findCategory).build();

        Schedule saveSchedule = scheduleDao.save(schedule);
        return new PostScheduleRes(saveSchedule.getId());
    }
}

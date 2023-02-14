package com.example.namo2.schedule;

import com.example.namo2.category.CategoryDao;
import com.example.namo2.config.BaseException;
import com.example.namo2.config.BaseResponseStatus;
import com.example.namo2.entity.Category;
import com.example.namo2.entity.Schedule;
import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.PostScheduleRes;
import com.example.namo2.schedule.dto.ScheduleDto;
import com.example.namo2.user.UserDao;
import com.example.namo2.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final UserDao userDao;
    private final CategoryDao categoryDao;

    public PostScheduleRes createSchedule(ScheduleDto scheduleDto, Long userId, Long categoryId) throws BaseException {
        try {
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
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.SCHEDULE_ILLEGAL_ARGUMENT_FAILURE);
        }
    }

    @Transactional(readOnly = true)
    public List<GetScheduleRes> findUsersSchedule(long userId) throws BaseException{
        try {
            User user = userDao.findById(userId).orElseThrow(InvalidParameterException::new);
            return scheduleDao.findSchedulesByUserId(user);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.SCHEDULE_ILLEGAL_ARGUMENT_FAILURE);
        }
    }
}

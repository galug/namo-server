package com.example.namo2.schedule;

import com.example.namo2.entity.Schedule;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.GetScheduleRes;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleDaoCustom {
    List<GetScheduleRes> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate);

    List<Schedule> findScheduleDiaryByMonthDtoWithNotPaging(User user, LocalDateTime startDate, LocalDateTime endTime);
}

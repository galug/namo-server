package com.example.namo2.schedule;

import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.GetScheduleRes;

import java.util.List;

public interface ScheduleDaoCustom {
    List<GetScheduleRes> findSchedulesByUserId(User user);
}

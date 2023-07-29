package com.example.namo2.schedule;

import com.example.namo2.entity.schedule.Schedule;
import com.example.namo2.entity.user.User;
import com.example.namo2.moim.dto.MoimScheduleRes;
import com.example.namo2.schedule.dto.GetScheduleRes;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<GetScheduleRes> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate);

    List<Schedule> findScheduleDiaryByMonthDtoWithNotPaging(User user, LocalDateTime startDate, LocalDateTime endTime);

    Schedule findScheduleAndImages(Long scheduleId);

    List<MoimScheduleRes> findScheduleInMoim(Long moimId, LocalDateTime startDate, LocalDateTime endDate);
}

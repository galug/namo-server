package com.example.namo2.schedule;

import com.example.namo2.entity.schedule.Schedule;
import com.example.namo2.entity.user.User;
import com.example.namo2.moim.dto.MoimScheduleRes;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.SliceDiaryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<GetScheduleRes> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate);

    SliceDiaryDto<DiaryDto> findScheduleDiaryByMonthDto(User user, LocalDateTime startDate, LocalDateTime endTime, Pageable pageable);

    Schedule findScheduleAndImages(Long scheduleId);

    List<MoimScheduleRes> findScheduleInMoim(Long moimId, LocalDateTime startDate, LocalDateTime endDate);
}

package com.example.namo2.domain.schedule;

import com.example.namo2.domain.entity.schedule.Schedule;
import com.example.namo2.domain.entity.user.User;
import com.example.namo2.domain.moim.dto.MoimScheduleRes;
import com.example.namo2.domain.schedule.dto.DiaryDto;
import com.example.namo2.domain.schedule.dto.GetScheduleRes;
import com.example.namo2.domain.schedule.dto.OnlyDiaryDto;
import com.example.namo2.domain.schedule.dto.SliceDiaryDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<GetScheduleRes> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate);

    SliceDiaryDto<DiaryDto> findScheduleDiaryByMonthDto(User user, LocalDateTime startDate, LocalDateTime endTime, Pageable pageable);

    List<OnlyDiaryDto> findAllScheduleDiary(User user);

    Schedule findOneScheduleAndImages(Long scheduleId);

    List<MoimScheduleRes> findMonthScheduleInMoim(Long moimId, LocalDateTime startDate, LocalDateTime endDate);

    public List<GetScheduleRes> findMoimSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate);
}

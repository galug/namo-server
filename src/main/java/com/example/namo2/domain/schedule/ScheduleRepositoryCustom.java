package com.example.namo2.domain.schedule;

import com.example.namo2.domain.memo.application.converter.MoimMemoResponseConverter;
import com.example.namo2.domain.memo.ui.dto.MoimMemoResponse;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleResponse;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.schedule.dto.GetScheduleRes;
import com.example.namo2.domain.schedule.dto.OnlyDiaryDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<GetScheduleRes> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate);

    MoimMemoResponse.SliceDiaryDto<MoimMemoResponse.DiaryDto> findScheduleDiaryByMonthDto(User user, LocalDateTime startDate, LocalDateTime endTime, Pageable pageable);

    List<OnlyDiaryDto> findAllScheduleDiary(User user);

    Schedule findOneScheduleAndImages(Long scheduleId);

    List<MoimScheduleResponse.MoimScheduleDto> findMonthScheduleInMoim(Long moimId, LocalDateTime startDate, LocalDateTime endDate);

    List<GetScheduleRes> findMoimSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate);
}

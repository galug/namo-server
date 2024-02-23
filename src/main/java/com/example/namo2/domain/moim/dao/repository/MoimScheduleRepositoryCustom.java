package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.schedule.ui.dto.ScheduleResponse;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MoimScheduleRepositoryCustom {
    ScheduleResponse.SliceDiaryDto findMoimScheduleMemoByMonth(Long userId, List<LocalDateTime> dates, Pageable pageable);
}

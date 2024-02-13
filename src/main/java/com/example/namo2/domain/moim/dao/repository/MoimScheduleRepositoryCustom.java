package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.schedule.dto.DiaryDto;
import com.example.namo2.domain.schedule.dto.SliceDiaryDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MoimScheduleRepositoryCustom {
    SliceDiaryDto<DiaryDto> findMoimScheduleMemoByMonth(Long userId, List<LocalDateTime> dates, Pageable pageable);
}

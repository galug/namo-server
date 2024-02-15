package com.example.namo2.domain.moim;

import com.example.namo2.domain.schedule.ui.dto.DiaryDto;
import com.example.namo2.domain.schedule.ui.dto.SliceDiaryDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MoimScheduleRepositoryCustom {
    public SliceDiaryDto<DiaryDto> findMoimScheduleMemoByMonth(Long userId, List<LocalDateTime> dates, Pageable pageable);
}

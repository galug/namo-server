package com.example.namo2.moim;

import com.example.namo2.entity.user.User;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.schedule.dto.SliceDiaryDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MoimScheduleRepositoryCustom {
    public SliceDiaryDto<DiaryDto> findMoimScheduleMemoByMonth(Long userId, List<LocalDateTime> dates, Pageable pageable);
}

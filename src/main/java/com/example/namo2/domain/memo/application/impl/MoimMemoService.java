package com.example.namo2.domain.memo.application.impl;

import com.example.namo2.domain.memo.dao.repository.MoimMemoRepository;
import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.schedule.dto.DiaryDto;
import com.example.namo2.domain.schedule.dto.SliceDiaryDto;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimMemoService {
    private final MoimMemoRepository moimMemoRepository;
    private final MoimScheduleRepository moimScheduleRepository;

    public SliceDiaryDto<DiaryDto> findMonth(Long userId, List<LocalDateTime> localDateTimes, Pageable pageable) {
        return moimScheduleRepository.findMoimScheduleMemoByMonth(userId, localDateTimes, pageable);
    }

    public Optional<MoimMemo> getMoimMemoOrNull(MoimSchedule moimSchedule) {
        return moimMemoRepository.findMoimMemoByMoimSchedule(moimSchedule);
    }

    public MoimMemo getMoimMemo(MoimSchedule moimSchedule) {
        return moimMemoRepository.findMoimMemoByMoimSchedule(moimSchedule)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_FAILURE));
    }

    public MoimMemo getMoimMemoWithUsers(MoimSchedule moimSchedule) {
        return moimMemoRepository.findMoimMemoAndUsersByMoimSchedule(moimSchedule);
    }

    public void removeMoimMemo(MoimMemo moimMemo) {
        moimMemoRepository.delete(moimMemo);
    }

    public MoimMemo create(MoimMemo moimMemo) {
        return moimMemoRepository.save(moimMemo);
    }
}

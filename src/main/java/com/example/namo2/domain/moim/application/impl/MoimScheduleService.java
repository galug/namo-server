package com.example.namo2.domain.moim.application.impl;

import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimScheduleService {
    private final MoimScheduleRepository moimScheduleRepository;
    public MoimSchedule create(MoimSchedule moimSchedule) {
        return moimScheduleRepository.save(moimSchedule);
    }

    public MoimSchedule getMoimSchedule(Long id) {
        return moimScheduleRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE));
    }
}

package com.example.namo2.domain.moim.application.impl;

import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.domain.MoimSchedule;
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
}

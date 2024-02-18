package com.example.namo2.domain.moim.application.impl;

import com.example.namo2.domain.moim.dao.repository.MoimScheduleAndUserRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleResponse;
import com.example.namo2.domain.schedule.ScheduleRepository;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimScheduleService {
    private final MoimScheduleRepository moimScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;

    public MoimSchedule create(MoimSchedule moimSchedule) {
        return moimScheduleRepository.save(moimSchedule);
    }

    public MoimSchedule getMoimSchedule(Long id) {
        return moimScheduleRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE));
    }

    public void delete(MoimSchedule moimSchedule) {
        moimScheduleRepository.delete(moimSchedule);
    }



    public List<MoimScheduleResponse.MoimScheduleDto> findMoimSchedulesOrigin(Long moimId, List<LocalDateTime> localDateTimes) {
        return scheduleRepository.findMonthScheduleInMoim(moimId, localDateTimes.get(0), localDateTimes.get(1));
    }

    public List<MoimScheduleAndUser> getMoimSchedules(
            List<LocalDateTime> localDateTimes, List<User> users) {
        return moimScheduleAndUserRepository
                .findMoimScheduleAndUserWithMoimScheduleByUsersAndDates(
                        localDateTimes.get(0), localDateTimes.get(1), users
                );
    }
}

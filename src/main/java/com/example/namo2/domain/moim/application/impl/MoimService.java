package com.example.namo2.domain.moim.application.impl;

import com.example.namo2.domain.category.dao.repository.CategoryRepository;
import com.example.namo2.domain.memo.MoimMemoRepository;
import com.example.namo2.domain.memo.MoimMemoService;
import com.example.namo2.domain.moim.dao.repository.MoimRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleAlarmRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleAndUserRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleRequest;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAlarm;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleDto;
import com.example.namo2.domain.schedule.ScheduleRepository;
import com.example.namo2.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_MOIM_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_USER_FAILURE;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimService {
    private final MoimRepository moimRepository;
    private final MoimScheduleRepository moimScheduleRepository;
    private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;
    private final MoimScheduleAlarmRepository moimScheduleAlarmRepository;
    private final MoimMemoRepository moimMemoRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ScheduleRepository scheduleRepository;

    private final MoimMemoService moimMemoService;

    public Moim create(Moim moim) {
        return moimRepository.save(moim);
    }

    public Moim getMoim(Long moimId) {
        return moimRepository.findById(moimId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MOIM_FAILURE));
    }

    public Moim getMoim(String code) {
        return moimRepository.findMoimByCode(code)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_FAILURE));
    }

    public List<MoimScheduleDto> findMoimSchedules(Long moimId, List<LocalDateTime> localDateTimes) {
        return scheduleRepository.findMonthScheduleInMoim(moimId, localDateTimes.get(0), localDateTimes.get(1));
    }

    @Transactional(readOnly = false)
    public void deleteSchedule(Long moimScheduleId) {
        MoimSchedule moimSchedule = moimScheduleRepository.findById(moimScheduleId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        MoimMemo moimMemo = moimMemoRepository.findMoimMemoAndLocationsByMoimSchedule(moimSchedule);

//         모임 메모가 있는 경우 모임 메모 장소를 모두 삭제 후 모임 메모 삭제
        if (moimMemo != null) {
            moimMemo.getMoimMemoLocations()
                    .stream()
                    .forEach((moimMemoLocation -> moimMemoService.deleteMoimMemoLocation(moimMemoLocation.getId())));
            moimMemoRepository.delete(moimMemo);
        }

        moimScheduleAndUserRepository.deleteMoimScheduleAndUserByMoimSchedule(moimSchedule);
        moimScheduleRepository.delete(moimSchedule);
    }
}

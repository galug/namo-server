package com.example.namo2.domain.moim.application.impl;

import com.example.namo2.domain.category.dao.repository.CategoryRepository;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.ui.dto.MoimCategoryDto;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleAndUserRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimScheduleAndUserService {
    private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;

    public void createAll(List<MoimScheduleAndUser> moimScheduleAndUsers) {
        moimScheduleAndUserRepository.saveAll(moimScheduleAndUsers);
    }

    public void removeMoimScheduleAndUser(MoimSchedule moimSchedule) {
        moimScheduleAndUserRepository.deleteMoimScheduleAndUserByMoimSchedule(moimSchedule);
    }

    public MoimScheduleAndUser getMoimScheduleAndUser(MoimSchedule moimSchedule, User user) {
        return moimScheduleAndUserRepository.findMoimScheduleAndUserByMoimScheduleAndUser(moimSchedule, user)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE));
    }
}

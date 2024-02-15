package com.example.namo2.domain.moim.application.impl;

import com.example.namo2.domain.moim.dao.repository.MoimAndUserRepository;
import com.example.namo2.domain.moim.domain.MoimAndUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimAndUserService {
    private final static int[] MOIM_USERS_COLOR = new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

    private final MoimAndUserRepository moimAndUserRepository;

    public MoimAndUser create(MoimAndUser moimAndUser) {
        return moimAndUserRepository.save(moimAndUser);
    }
}

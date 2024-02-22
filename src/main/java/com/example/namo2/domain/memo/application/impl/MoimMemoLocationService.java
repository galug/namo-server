package com.example.namo2.domain.memo.application.impl;

import com.example.namo2.domain.memo.dao.repository.MoimMemoLocationAndUserRepository;
import com.example.namo2.domain.memo.dao.repository.MoimMemoLocationImgRepository;
import com.example.namo2.domain.memo.dao.repository.MoimMemoLocationRepository;
import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoimMemoLocationService {
    private final MoimMemoLocationRepository moimMemoLocationRepository;
    private final MoimMemoLocationAndUserRepository moimMemoLocationAndUserRepository;
    private final MoimMemoLocationImgRepository moimMemoLocationImgRepository;

    public MoimMemoLocation createMoimMemoLocation(MoimMemoLocation moimMemoLocation) {
        return moimMemoLocationRepository.save(moimMemoLocation);
    }

    public List<MoimMemoLocationAndUser> createMoimMemoLocationAndUsers(List<MoimMemoLocationAndUser> moimMemoLocations) {
        return moimMemoLocationAndUserRepository.saveAll(moimMemoLocations);
    }

    public MoimMemoLocationImg createMoimMemoLocationImg(MoimMemoLocationImg moimMemoLocationImg) {
        return moimMemoLocationImgRepository.save(moimMemoLocationImg);
    }
}

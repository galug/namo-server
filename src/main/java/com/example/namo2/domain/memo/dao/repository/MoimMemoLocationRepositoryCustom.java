package com.example.namo2.domain.memo.dao.repository;

import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.moim.domain.MoimSchedule;

import java.util.List;

public interface MoimMemoLocationRepositoryCustom {
    List<MoimMemoLocation> findMoimMemoLocationsWithImgs(MoimSchedule moimSchedule);

    List<MoimMemoLocationAndUser> findMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocations);
}

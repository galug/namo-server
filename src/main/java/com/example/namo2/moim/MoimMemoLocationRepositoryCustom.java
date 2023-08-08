package com.example.namo2.moim;

import com.example.namo2.moim.dto.MoimMemoLocationDto;

import java.util.List;

public interface MoimMemoLocationRepositoryCustom {
    List<MoimMemoLocationDto> findMoimMemo(Long moimScheduleId);
}

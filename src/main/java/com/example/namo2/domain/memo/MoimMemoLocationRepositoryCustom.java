package com.example.namo2.domain.memo;

import com.example.namo2.domain.moim.dto.MoimMemoLocationDto;

import java.util.List;

public interface MoimMemoLocationRepositoryCustom {
    List<MoimMemoLocationDto> findMoimMemo(Long moimScheduleId);
}

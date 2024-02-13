package com.example.namo2.domain.memo;

import com.example.namo2.domain.moim.ui.dto.MoimMemoLocationDto;

import java.util.List;

public interface MoimMemoLocationRepositoryCustom {
    List<MoimMemoLocationDto> findMoimMemo(Long moimScheduleId);
}

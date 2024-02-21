package com.example.namo2.domain.memo.dao.repository;

import com.example.namo2.domain.memo.ui.dto.MoimMemoResponse;

import java.util.List;

public interface MoimMemoLocationRepositoryCustom {
    List<MoimMemoResponse.MoimMemoLocationDto> findMoimMemo(Long moimScheduleId);
}

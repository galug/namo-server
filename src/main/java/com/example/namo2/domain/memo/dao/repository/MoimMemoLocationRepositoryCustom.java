package com.example.namo2.domain.memo.dao.repository;

import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.ui.dto.MoimMemoResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoimMemoLocationRepositoryCustom {
    List<MoimMemoResponse.MoimMemoLocationDto> findMoimMemo(Long moimScheduleId);

    @Query("select mml " +
            "from MoimMemoLocation mml " +
            "where mml.moimMemo = :moimMemo")
    List<MoimMemoLocation> findMoimMemo(@Param("moimMemo") MoimMemo moimMemo);
}

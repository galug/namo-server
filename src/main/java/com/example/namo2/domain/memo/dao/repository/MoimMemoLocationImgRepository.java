package com.example.namo2.domain.memo.dao.repository;

import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoimMemoLocationImgRepository extends JpaRepository<MoimMemoLocationImg, Long> {
    @Modifying
    @Query("delete from MoimMemoLocationImg mli where mli.moimMemoLocation = :moimMemoLocation")
    void deleteMoimMemoLocationImgByMoimMemoLocation(MoimMemoLocation moimMemoLocation);

    @Query("select mli " +
            "from MoimMemoLocationImg mli " +
            "where mli.moimMemoLocation in :moimMemoLocations")
    List<MoimMemoLocationImg> findMoimMemoLocationImgsByMoimMemoLocations(List<MoimMemoLocation> moimMemoLocations);
}

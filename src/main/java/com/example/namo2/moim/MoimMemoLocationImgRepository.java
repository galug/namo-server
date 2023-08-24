package com.example.namo2.moim;

import com.example.namo2.entity.moimmemo.MoimMemoLocation;
import com.example.namo2.entity.moimmemo.MoimMemoLocationImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MoimMemoLocationImgRepository extends JpaRepository<MoimMemoLocationImg, Long> {
    @Modifying
    @Query("delete from MoimMemoLocationImg mli where mli.moimMemoLocation = :moimMemoLocation")
    void deleteMoimMemoLocationImgByMoimMemoLocation(MoimMemoLocation moimMemoLocation);
}

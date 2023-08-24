package com.example.namo2.moim;

import com.example.namo2.entity.moimmemo.MoimMemo;
import com.example.namo2.entity.moimmemo.MoimMemoLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MoimMemoLocationRepository extends JpaRepository<MoimMemoLocation, Long>, MoimMemoLocationRepositoryCustom {

    @Query(value = "select ml" +
            " from MoimMemoLocation ml" +
            " join fetch ml.moimMemoLocationImgs" +
            " where ml.id = :locationId")
    Optional<MoimMemoLocation> findMoimMemoLocationById(@Param("locationId") Long locationId);
}

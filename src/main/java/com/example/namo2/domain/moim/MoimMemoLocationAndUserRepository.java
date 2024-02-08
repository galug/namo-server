package com.example.namo2.domain.moim;

import com.example.namo2.domain.entity.moimmemo.MoimMemoLocation;
import com.example.namo2.domain.entity.moimmemo.MoimMemoLocationAndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MoimMemoLocationAndUserRepository extends JpaRepository<MoimMemoLocationAndUser, Long> {
    @Modifying
    @Query("delete from MoimMemoLocationAndUser lau where lau.moimMemoLocation = :moimMemoLocation")
    void deleteMoimMemoLocationAndUserByMoimMemoLocation(MoimMemoLocation moimMemoLocation);
}

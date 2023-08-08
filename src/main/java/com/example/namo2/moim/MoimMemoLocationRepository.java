package com.example.namo2.moim;

import com.example.namo2.entity.moimmemo.MoimMemo;
import com.example.namo2.entity.moimmemo.MoimMemoLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimMemoLocationRepository extends JpaRepository<MoimMemoLocation, Long>, MoimMemoLocationRepositoryCustom {

    
}

package com.example.namo2.moim;

import com.example.namo2.entity.moimmemo.MoimMemo;
import com.example.namo2.entity.moimschedule.MoimSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimMemoRepository extends JpaRepository<MoimMemo, Long> {
    public boolean existsMoimMemoByMoimSchedule(MoimSchedule moimSchedule);
}

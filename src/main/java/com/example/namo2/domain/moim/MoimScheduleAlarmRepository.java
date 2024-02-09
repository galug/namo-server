package com.example.namo2.domain.moim;

import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MoimScheduleAlarmRepository extends JpaRepository<MoimScheduleAlarm, Long> {
    @Modifying
    @Query("delete from MoimScheduleAlarm msa where msa.moimSchedule =:moimSchedule")
    void deleteMoimScheduleAlarmByMoimSchedule(MoimSchedule moimSchedule);
}

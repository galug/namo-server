package com.example.namo2.moim;

import com.example.namo2.entity.moimschedule.MoimSchedule;
import com.example.namo2.entity.moimschedule.MoimScheduleAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MoimScheduleAlarmRepository extends JpaRepository<MoimScheduleAlarm, Long> {
    @Modifying
    @Query("delete from MoimScheduleAlarm msa where msa.moimSchedule =:moimSchedule")
    void deleteMoimScheduleAlarmByMoimSchedule(MoimSchedule moimSchedule);
}

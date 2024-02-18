package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAlarm;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoimScheduleAlarmRepository extends JpaRepository<MoimScheduleAlarm, Long> {
    @Modifying
    @Query("delete from MoimScheduleAlarm msa where msa.moimScheduleAndUser =:moimScheduleAndUser")
    void deleteMoimScheduleAlarmByMoimScheduleAndUser(@Param("moimScheduleAndUser") MoimScheduleAndUser moimScheduleAndUser);

    @Modifying
    @Query("delete from MoimScheduleAlarm msa where msa.moimScheduleAndUser in :moimScheduleAndUser")
    void deleteMoimScheduleAlarmByMoimScheduleAndUser(@Param("moimScheduleAndUser") List<MoimScheduleAndUser> moimScheduleAndUser);
}

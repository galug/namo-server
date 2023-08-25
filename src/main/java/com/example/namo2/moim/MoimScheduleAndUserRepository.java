package com.example.namo2.moim;

import com.example.namo2.entity.moimschedule.MoimSchedule;
import com.example.namo2.entity.moimschedule.MoimScheduleAndUser;
import com.example.namo2.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MoimScheduleAndUserRepository extends JpaRepository<MoimScheduleAndUser, Long> {
    @Modifying
    @Query("delete from MoimScheduleAndUser mau" +
            " where mau.moimSchedule = :moimSchedule")
    void deleteMoimScheduleAndUserByMoimSchedule(MoimSchedule moimSchedule);

    Optional<MoimScheduleAndUser> findMoimScheduleAndUserByMoimScheduleAndUser(MoimSchedule moimSchedule, User user);
}

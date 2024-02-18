package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleResponse;
import com.example.namo2.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MoimScheduleAndUserRepository extends JpaRepository<MoimScheduleAndUser, Long>, MoimScheduleAndUserRepositoryCustom {
    @Modifying
    @Query("delete from MoimScheduleAndUser mau" +
            " where mau.moimSchedule = :moimSchedule")
    void deleteMoimScheduleAndUserByMoimSchedule(MoimSchedule moimSchedule);

    Optional<MoimScheduleAndUser> findMoimScheduleAndUserByMoimScheduleAndUser(MoimSchedule moimSchedule, User user);

    @Query("select msu from MoimScheduleAndUser msu where msu.moimSchedule = :moimSchedule")
    List<MoimScheduleAndUser> findMoimScheduleAndUserByMoimSchedule(@Param("moimSchedule") MoimSchedule moimSchedule);
}

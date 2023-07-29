package com.example.namo2.moim;

import com.example.namo2.entity.moimschedule.MoimScheduleAndUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimScheduleAndUserRepository extends JpaRepository<MoimScheduleAndUser, Long> {
}

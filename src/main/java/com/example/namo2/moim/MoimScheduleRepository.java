package com.example.namo2.moim;

import com.example.namo2.entity.moimschedule.MoimSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MoimScheduleRepository extends JpaRepository<MoimSchedule, Long> {
}

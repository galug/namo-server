package com.example.namo2.schedule;

import com.example.namo2.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleDao extends JpaRepository<Schedule, Long> {
}

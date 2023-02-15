package com.example.namo2.schedule;

import com.example.namo2.entity.Schedule;
import com.example.namo2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleDao extends JpaRepository<Schedule, Long>, ScheduleDaoCustom {
}

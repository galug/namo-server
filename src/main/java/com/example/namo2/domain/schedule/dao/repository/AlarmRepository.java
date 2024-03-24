package com.example.namo2.domain.schedule.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.schedule.domain.Alarm;
import com.example.namo2.domain.schedule.domain.Schedule;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	void deleteAllBySchedule(Schedule schedule);
}

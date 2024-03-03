package com.example.namo2.domain.schedule.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.schedule.domain.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}

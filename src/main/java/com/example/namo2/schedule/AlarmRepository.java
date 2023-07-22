package com.example.namo2.schedule;

import com.example.namo2.entity.schedule.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}

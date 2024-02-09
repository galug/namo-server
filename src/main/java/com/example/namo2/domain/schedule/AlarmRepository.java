package com.example.namo2.domain.schedule;

import com.example.namo2.domain.schedule.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}

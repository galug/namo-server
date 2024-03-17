package com.example.namo2.domain.schedule.application.impl;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.schedule.dao.repository.AlarmRepository;
import com.example.namo2.domain.schedule.domain.Schedule;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {
	private final AlarmRepository alarmRepository;
	public void removeAlarmsBySchedule(Schedule schedule){
		alarmRepository.deleteAllBySchedule(schedule);
	}
}

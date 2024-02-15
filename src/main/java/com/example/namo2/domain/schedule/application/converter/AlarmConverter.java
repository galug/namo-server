package com.example.namo2.domain.schedule.application.converter;

import java.util.List;

import com.example.namo2.domain.schedule.domain.Alarm;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.schedule.ui.dto.ScheduleRequest;

public class AlarmConverter {

	public static List<Alarm> toAlarms(ScheduleRequest.PostScheduleReq req, Schedule schedule){
		return req.getAlarmDate().stream().map(alarm ->
			Alarm.builder()
				.alarmDate(alarm)
				.schedule(schedule)
				.build()
		).toList();
	}
}

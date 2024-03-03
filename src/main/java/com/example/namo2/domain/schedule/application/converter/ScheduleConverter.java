package com.example.namo2.domain.schedule.application.converter;

import com.example.namo2.domain.category.domain.Category;

import com.example.namo2.domain.schedule.domain.Period;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.schedule.ui.dto.ScheduleRequest;

import com.example.namo2.domain.user.domain.User;

public class ScheduleConverter {
	public static Period toPeriod(ScheduleRequest.PostScheduleDto dto) {
		return Period.builder()
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.dayInterval(dto.getInterval())
			.build();
	}

	public static Schedule toSchedule(ScheduleRequest.PostScheduleDto dto, Period period, User user,
		Category category) {
		return Schedule.builder()
			.name(dto.getName())
			.period(period)
			.x(dto.getX())
			.y(dto.getY())
			.locationName(dto.getLocationName())
			.user(user)
			.category(category)
			.build();
	}
}

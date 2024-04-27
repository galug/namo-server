package com.example.namo2.domain.moim.application.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.namo2.domain.individual.domain.Category;

import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAlarm;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleRequest;

import com.example.namo2.domain.individual.domain.constant.Location;
import com.example.namo2.domain.individual.domain.constant.Period;

import com.example.namo2.domain.user.domain.User;

public class MoimScheduleConverter {
	private MoimScheduleConverter() {
		throw new IllegalStateException("Util Classes");
	}

	public static Period toPeriod(MoimScheduleRequest.PostMoimScheduleDto moimScheduleDto) {
		return Period.builder()
			.startDate(moimScheduleDto.getStartDate())
			.endDate(moimScheduleDto.getEndDate())
			.dayInterval(moimScheduleDto.getInterval())
			.build();
	}

	public static Period toPeriod(MoimScheduleRequest.PatchMoimScheduleDto moimScheduleDto) {
		return Period.builder()
			.startDate(moimScheduleDto.getStartDate())
			.endDate(moimScheduleDto.getEndDate())
			.dayInterval(moimScheduleDto.getInterval())
			.build();
	}

	public static Location toLocation(MoimScheduleRequest.PostMoimScheduleDto moimScheduleDto) {
		return Location.builder()
			.x(moimScheduleDto.getX())
			.y(moimScheduleDto.getY())
			.locationName(moimScheduleDto.getLocationName())
			.kakaoLocationId(moimScheduleDto.getKakaoLocationId())
			.build();
	}

	public static Location toLocation(MoimScheduleRequest.PatchMoimScheduleDto moimScheduleDto) {
		return Location.builder()
			.x(moimScheduleDto.getX())
			.y(moimScheduleDto.getY())
			.locationName(moimScheduleDto.getLocationName())
			.build();
	}

	public static MoimSchedule toMoimSchedule(Moim moim, Period period, Location location,
		MoimScheduleRequest.PostMoimScheduleDto moimScheduleDto) {
		return MoimSchedule.builder()
			.name(moimScheduleDto.getName())
			.period(period)
			.location(location)
			.moim(moim)
			.build();
	}

	public static List<MoimScheduleAndUser> toMoimScheduleAndUsers(
		List<Category> categories,
		MoimSchedule moimSchedule,
		List<User> users
	) {
		Map<User, Category> categoryMap = categories
			.stream().collect(Collectors.toMap(Category::getUser, category -> category));

		return users.stream()
			.map((user) -> toMoimScheduleAndUser(user, moimSchedule, categoryMap.get(user)))
			.collect(Collectors.toList());
	}

	public static MoimScheduleAndUser toMoimScheduleAndUser(User user, MoimSchedule moimSchedule, Category category) {
		return MoimScheduleAndUser.builder()
			.user(user)
			.moimSchedule(moimSchedule)
			.category(category)
			.build();
	}

	public static MoimScheduleAlarm toMoimScheduleAlarm(MoimScheduleAndUser moimScheduleAndUser, Integer alarmDate) {
		return MoimScheduleAlarm.builder()
			.alarmDate(alarmDate)
			.moimScheduleAndUser(moimScheduleAndUser)
			.build();
	}
}

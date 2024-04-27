package com.example.namo2.domain.individual.application.converter;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;

import com.example.namo2.domain.moim.domain.MoimScheduleAlarm;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;

import com.example.namo2.domain.individual.domain.Alarm;
import com.example.namo2.domain.individual.domain.Schedule;
import com.example.namo2.domain.individual.ui.dto.ScheduleResponse;

public class ScheduleResponseConverter {
	public static ScheduleResponse.ScheduleIdDto toScheduleIdRes(Schedule schedule) {
		return ScheduleResponse.ScheduleIdDto.builder()
			.scheduleId(schedule.getId())
			.build();
	}

	public static ScheduleResponse.GetScheduleDto toGetScheduleRes(Schedule schedule) {
		Long startDate = schedule.getPeriod()
			.getStartDate()
			.atZone(ZoneId.systemDefault())
			.toInstant()
			.getEpochSecond();
		Long endDate = schedule.getPeriod().getEndDate().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
		List<Integer> alarmDates = schedule.getAlarms().stream().map(Alarm::getAlarmDate).toList();

		return ScheduleResponse.GetScheduleDto.builder()
			.scheduleId(schedule.getId())
			.name(schedule.getName())
			.startDate(startDate)
			.endDate(endDate)
			.alarmDate(alarmDates)
			.interval(schedule.getPeriod().getDayInterval())
			.x(schedule.getLocation().getX())
			.y(schedule.getLocation().getY())
			.locationName(schedule.getLocation().getLocationName())
			.kakaoLocationId(schedule.getLocation().getKakaoLocationId())
			.categoryId(schedule.getCategory().getId())
			.hasDiary(schedule.getHasDiary())
			.isMoimSchedule(false)
			.build();
	}

	public static ScheduleResponse.GetScheduleDto toGetScheduleRes(MoimScheduleAndUser moimScheduleAndUser) {
		Long startDate = moimScheduleAndUser.getMoimSchedule().getPeriod().getStartDate()
			.atZone(ZoneId.systemDefault())
			.toInstant()
			.getEpochSecond();
		Long endDate = moimScheduleAndUser.getMoimSchedule().getPeriod().getEndDate()
			.atZone(ZoneId.systemDefault())
			.toInstant()
			.getEpochSecond();
		List<Integer> alarmDates = moimScheduleAndUser.getMoimScheduleAlarms().stream()
			.map(MoimScheduleAlarm::getAlarmDate).toList();

		return ScheduleResponse.GetScheduleDto.builder()
			.scheduleId(moimScheduleAndUser.getMoimSchedule().getId())
			.name(moimScheduleAndUser.getMoimSchedule().getName())
			.startDate(startDate)
			.endDate(endDate)
			.alarmDate(alarmDates)
			.interval(moimScheduleAndUser.getMoimSchedule().getPeriod().getDayInterval())
			.x(moimScheduleAndUser.getMoimSchedule().getLocation().getX())
			.y(moimScheduleAndUser.getMoimSchedule().getLocation().getY())
			.locationName(moimScheduleAndUser.getMoimSchedule().getLocation().getLocationName())
			.kakaoLocationId(moimScheduleAndUser.getMoimSchedule().getLocation().getKakaoLocationId())
			.categoryId(moimScheduleAndUser.getCategory().getId())
			.hasDiary(moimScheduleAndUser.getMoimSchedule().getMoimMemo() != null)
			.isMoimSchedule(true)
			.build();
	}

	public static ScheduleResponse.GetDiaryByUserDto toGetDiaryByUserRes(Schedule schedule) {
		return ScheduleResponse.GetDiaryByUserDto.builder()
			.scheduleId(schedule.getId())
			.contents(schedule.getContents())
			.urls(schedule.getImages().stream()
				.map(image -> image.getImgUrl())
				.collect(Collectors.toList()))
			.build();
	}

	public static ScheduleResponse.GetDiaryByScheduleDto toGetDiaryByScheduleRes(Schedule schedule,
		List<String> imgUrls) {
		return ScheduleResponse.GetDiaryByScheduleDto.builder()
			.contents(schedule.getContents())
			.urls(imgUrls)
			.build();
	}

	public static ScheduleResponse.SliceDiaryDto toSliceDiaryDto(Slice<ScheduleResponse.DiaryDto> slice) {
		return ScheduleResponse.SliceDiaryDto.builder()
			.content(slice.getContent())
			.currentPage(slice.getNumber())
			.size(slice.getSize())
			.first(slice.isFirst())
			.last(slice.isLast())
			.build();
	}

	public static ScheduleResponse.DiaryDto toDiaryDto(Schedule schedule) {
		return ScheduleResponse.DiaryDto.builder()
			.scheduleId(schedule.getId())
			.name(schedule.getName())
			.startDate(schedule.getPeriod().getStartDate()
				.atZone(ZoneId.systemDefault())
				.toInstant()
				.getEpochSecond())
			.contents(schedule.getContents())
			.categoryId(schedule.getCategory().getId())
			.color(schedule.getCategory().getPalette().getId())
			.placeName(schedule.getLocation().getLocationName())
			.urls(schedule.getImages().stream()
				.map(image -> image.getImgUrl())
				.collect(Collectors.toList()))
			.build();
	}
}

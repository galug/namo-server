package com.example.namo2.domain.moim.application.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleResponse;

import com.example.namo2.domain.schedule.domain.Schedule;

import com.example.namo2.domain.user.domain.User;

public class MoimScheduleResponseConverter {
	private MoimScheduleResponseConverter() {
		throw new IllegalStateException("Util Classes");
	}

	public static List<MoimScheduleResponse.MoimScheduleDto> toMoimScheduleDtos(
		List<Schedule> indivisualsSchedules,
		List<MoimScheduleAndUser> moimScheduleAndUsers,
		List<MoimAndUser> moimAndUsers) {
		List<MoimScheduleResponse.MoimScheduleDto> result = getMoimScheduleDtos(indivisualsSchedules, moimAndUsers);

		Map<User, MoimScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtoMap = getMoimScheduleUserDtoMap(
			moimAndUsers);
		Map<MoimSchedule, List<MoimScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap
			= getMoimScheduleMappingUserDtoMap(moimScheduleAndUsers, moimScheduleUserDtoMap);
		addMoimSchedulesToResult(moimAndUsers, result, moimScheduleMappingUserDtoMap);
		return result;
	}

	private static List<MoimScheduleResponse.MoimScheduleDto> getMoimScheduleDtos(List<Schedule> indivisualsSchedules,
		List<MoimAndUser> moimAndUsers) {
		Map<User, Integer> usersColor = moimAndUsers.stream().collect(
			Collectors.toMap(
				MoimAndUser::getUser, MoimAndUser::getColor
			));
		List<MoimScheduleResponse.MoimScheduleDto> result = indivisualsSchedules.stream()
			.map((schedule -> toMoimScheduleDto(schedule, usersColor.get(schedule.getUser()))))
			.collect(Collectors.toList());
		return result;
	}

	private static Map<User, MoimScheduleResponse.MoimScheduleUserDto> getMoimScheduleUserDtoMap(
		List<MoimAndUser> moimAndUsers) {
		return moimAndUsers.stream()
			.collect(Collectors.toMap(
				MoimAndUser::getUser,
				(moimAndUser -> toMoimScheduleUserDto(moimAndUser))
			));
	}

	private static MoimScheduleResponse.MoimScheduleUserDto toMoimScheduleUserDto(MoimAndUser moimAndUser) {
		return MoimScheduleResponse.MoimScheduleUserDto
			.builder()
			.userId(moimAndUser.getUser().getId())
			.userName(moimAndUser.getUser().getName())
			.color(moimAndUser.getColor())
			.build();
	}

	private static Map<MoimSchedule, List<MoimScheduleResponse.MoimScheduleUserDto>> getMoimScheduleMappingUserDtoMap(
		List<MoimScheduleAndUser> moimScheduleAndUsers,
		Map<User, MoimScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtoMap) {
		return moimScheduleAndUsers.stream().collect(
			Collectors.groupingBy(
				(MoimScheduleAndUser::getMoimSchedule),
				Collectors.mapping(
					(moimScheduleAndUser -> moimScheduleUserDtoMap.get(moimScheduleAndUser.getUser())),
					Collectors.toList()
				)
			)
		);
	}

	private static void addMoimSchedulesToResult(List<MoimAndUser> moimAndUsers,
		List<MoimScheduleResponse.MoimScheduleDto> result,
		Map<MoimSchedule, List<MoimScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap) {
		for (MoimSchedule moimSchedule : moimScheduleMappingUserDtoMap.keySet()) {
			MoimScheduleResponse.MoimScheduleDto moimScheduleDto = toMoimScheduleDto(moimSchedule);
			moimScheduleDto.setUsers(
				moimScheduleMappingUserDtoMap.get(moimSchedule),
				moimSchedule.getMoim() == moimAndUsers.get(0).getMoim(),
				moimSchedule.getMoimMemo() != null
			);
			result.add(moimScheduleDto);
		}
	}

	public static MoimScheduleResponse.MoimScheduleDto toMoimScheduleDto(Schedule schedule, Integer color) {
		return new MoimScheduleResponse.MoimScheduleDto(
			schedule.getName(),
			schedule.getPeriod().getStartDate(),
			schedule.getPeriod().getEndDate(),
			schedule.getPeriod().getDayInterval(),
			schedule.getUser().getId(),
			schedule.getUser().getName(),
			color
		);
	}

	public static MoimScheduleResponse.MoimScheduleDto toMoimScheduleDto(MoimSchedule moimSchedule) {
		return new MoimScheduleResponse.MoimScheduleDto(
			moimSchedule.getName(),
			moimSchedule.getPeriod().getStartDate(),
			moimSchedule.getPeriod().getEndDate(),
			moimSchedule.getPeriod().getDayInterval(),
			moimSchedule.getMoim().getId(),
			moimSchedule.getId(),
			moimSchedule.getLocation().getX(),
			moimSchedule.getLocation().getY(),
			moimSchedule.getLocation().getLocationName(),
			moimSchedule.getLocation().getKakaoLocationId()
		);
	}

}

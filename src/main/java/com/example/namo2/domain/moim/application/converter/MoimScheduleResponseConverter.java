package com.example.namo2.domain.moim.application.converter;

import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleResponse;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MoimScheduleResponseConverter {
    private MoimScheduleResponseConverter() {
        throw new IllegalStateException("Util Classes");
    }

    public static List<MoimScheduleResponse.MoimScheduleDto> toMoimScheduleDtos(
            List<Schedule> indivisualsSchedules,
            List<MoimScheduleAndUser> moimScheduleAndUsers,
            List<MoimAndUser> moimAndUsers) {
        Map<User, Integer> usersColor = moimAndUsers.stream().collect(
                Collectors.toMap(
                        MoimAndUser::getUser, MoimAndUser::getColor
                ));
        List<MoimScheduleResponse.MoimScheduleDto> result = indivisualsSchedules.stream()
                .map((schedule -> toMoimScheduleDto(schedule, usersColor.get(schedule.getUser()))))
                .collect(Collectors.toList());

        Map<User, MoimScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtoMap = moimAndUsers.stream()
                .collect(Collectors.toMap(
                        MoimAndUser::getUser,
                        (moimAndUser -> toMoimScheduleUserDto(moimAndUser))
                ));

        Map<MoimSchedule, List<MoimScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap = moimScheduleAndUsers.stream().collect(
                Collectors.groupingBy(
                        (MoimScheduleAndUser::getMoimSchedule),
                        Collectors.mapping(
                                (moimScheduleAndUser -> moimScheduleUserDtoMap.get(moimScheduleAndUser.getUser())),
                                Collectors.toList()
                        )
                )
        );

        for (MoimSchedule moimSchedule : moimScheduleMappingUserDtoMap.keySet()) {
            MoimScheduleResponse.MoimScheduleDto moimScheduleDto = toMoimScheduleDto(moimSchedule);
            moimScheduleDto.setUsers(
                    moimScheduleMappingUserDtoMap.get(moimSchedule),
                    moimSchedule.getMoim() == moimAndUsers.get(0).getMoim(),
                    moimSchedule.getMoimMemo() != null
            );
            result.add(moimScheduleDto);
        }
        return result;
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
                moimSchedule.getLocation().getLocationName()
        );
    }

    public static MoimScheduleResponse.MoimScheduleUserDto toMoimScheduleUserDto(MoimAndUser moimAndUser) {
        return new MoimScheduleResponse.MoimScheduleUserDto(
                moimAndUser.getUser().getId(),
                moimAndUser.getUser().getName(),
                moimAndUser.getColor()
        );
    }
}

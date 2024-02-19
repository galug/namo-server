package com.example.namo2.domain.moim.application.converter;

import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MoimResponseConverter {
    private MoimResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static MoimResponse.MoimIdDto toMoimIdDto(Moim moim) {
        return MoimResponse.MoimIdDto.builder()
                .moimId(moim.getId())
                .build();
    }

    public static List<MoimResponse.MoimDto> toMoimDtos(List<MoimAndUser> moimAndUsers) {
        return moimAndUsers.stream()
                .collect(
                        Collectors.groupingBy(
                                MoimAndUser::getMoim
                        )
                )
                .entrySet().stream()
                .map((entrySet) -> toMoimDto(entrySet.getKey(), entrySet.getValue()))
                .collect(Collectors.toList());
    }

    public static MoimResponse.MoimDto toMoimDto(Moim moim, List<MoimAndUser> moimAndUsers) {
        return MoimResponse.MoimDto.builder()
                .groupId(moim.getId())
                .groupName(moim.getName())
                .groupImgUrl(moim.getImgUrl())
                .groupCode(moim.getCode())
                .moimUsers(toMoimUserDtos(moimAndUsers))
                .build();
    }

    private static List<MoimResponse.MoimUserDto> toMoimUserDtos(List<MoimAndUser> moimAndUsers) {
        return moimAndUsers.stream()
                .map(MoimResponseConverter::toMoimUserDto)
                .collect(Collectors.toList());
    }

    private static MoimResponse.MoimUserDto toMoimUserDto(MoimAndUser moimAndUser) {
        return MoimResponse.MoimUserDto
                .builder()
                .userId(moimAndUser.getUser().getId())
                .userName(moimAndUser.getUser().getName())
                .color(moimAndUser.getColor())
                .build();
    }
}

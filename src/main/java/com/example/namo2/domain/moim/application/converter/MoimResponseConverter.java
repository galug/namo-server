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
        return new MoimResponse.MoimIdDto(moim.getId());
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
        return new MoimResponse.MoimDto(moim.getId()
                , moim.getName()
                , moim.getImgUrl()
                , moim.getCode()
                , toMoimUserDtos(moimAndUsers));
    }

    private static List<MoimResponse.MoimUserDto> toMoimUserDtos(List<MoimAndUser> moimAndUsers) {
        return moimAndUsers.stream()
                .map(MoimResponseConverter::toMoimUserDto)
                .collect(Collectors.toList());
    }

    private static MoimResponse.MoimUserDto toMoimUserDto(MoimAndUser moimAndUser) {
        return new MoimResponse.MoimUserDto(moimAndUser.getUser().getId(), moimAndUser.getUser().getName(), moimAndUser.getColor());
    }
}

package com.example.namo2.domain.group.application.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.namo2.domain.group.domain.Moim;
import com.example.namo2.domain.group.domain.MoimAndUser;
import com.example.namo2.domain.group.ui.dto.MoimResponse;

public class MoimResponseConverter {
	private MoimResponseConverter() {
		throw new IllegalStateException("Util Class");
	}

	public static MoimResponse.MoimIdDto toMoimIdDto(Moim moim) {
		return MoimResponse.MoimIdDto.builder()
			.moimId(moim.getId())
			.build();
	}

	public static List<MoimResponse.MoimDto> toMoimDtos(List<MoimAndUser> moimAndUsers,
		List<MoimAndUser> curUserMoimsInUser) {
		Map<Moim, List<MoimAndUser>> moimMappingMoimAndUsers = moimAndUsers.stream()
			.collect(
				Collectors.groupingBy(
					MoimAndUser::getMoim
				)
			);
		return curUserMoimsInUser.stream()
			.map((moimAndUser) -> toMoimDto(moimAndUser, moimMappingMoimAndUsers.get(moimAndUser.getMoim())))
			.collect(Collectors.toList());
	}

	public static MoimResponse.MoimDto toMoimDto(MoimAndUser moimAndUser, List<MoimAndUser> moimAndUsers) {
		return MoimResponse.MoimDto.builder()
			.groupId(moimAndUser.getMoim().getId())
			.groupName(moimAndUser.getMoimCustomName())
			.groupImgUrl(moimAndUser.getMoim().getImgUrl())
			.groupCode(moimAndUser.getMoim().getCode())
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

	public static MoimResponse.MoimParticipantDto toMoimParticipantDto(Moim moim) {
		return MoimResponse.MoimParticipantDto.builder()
			.moimId(moim.getId())
			.code(moim.getCode())
			.build();
	}
}

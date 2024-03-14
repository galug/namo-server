package com.example.namo2.domain.moim.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.namo2.domain.moim.application.converter.MoimAndUserConverter;
import com.example.namo2.domain.moim.application.converter.MoimConverter;
import com.example.namo2.domain.moim.application.converter.MoimResponseConverter;
import com.example.namo2.domain.moim.application.impl.MoimAndUserService;
import com.example.namo2.domain.moim.application.impl.MoimService;
import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimRequest;
import com.example.namo2.domain.moim.ui.dto.MoimResponse;

import com.example.namo2.domain.user.application.impl.UserService;
import com.example.namo2.domain.user.domain.User;

import com.example.namo2.global.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MoimFacade {
	/**
	 * TODO
	 * ISSUE 설명: Moim_USER_COLOR 에 대한 부여를 현재 총 모임원의 index를 통해서 부여함
	 * 이 경우 모임원이 탈퇴하고 다시금 들어올 경우 동일한 color를 부여받는 모임원이 생김
	 */
	private static final int[] MOIM_USERS_COLOR = new int[] {5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
	private final MoimService moimService;
	private final UserService userService;
	private final MoimAndUserService moimAndUserService;
	private final FileUtils fileUtils;

	@Transactional(readOnly = false)
	public MoimResponse.MoimIdDto createMoim(Long userId, String groupName, MultipartFile img) {
		User user = userService.getUser(userId);
		String url = fileUtils.uploadImage(img);
		Moim moim = MoimConverter.toMoim(groupName, url);
		moimService.create(moim);

		MoimAndUser moimAndUser = MoimAndUserConverter
			.toMoimAndUser(groupName, MOIM_USERS_COLOR[0], user, moim);
		moimAndUserService.create(moimAndUser);
		return MoimResponseConverter.toMoimIdDto(moim);
	}

	@Transactional(readOnly = true)
	public List<MoimResponse.MoimDto> getMoims(Long userId) {
		User user = userService.getUser(userId);
		List<Moim> moimsInUser = moimAndUserService.getMoimAndUsers(user)
			.stream().map(MoimAndUser::getMoim)
			.collect(Collectors.toList());

		List<MoimAndUser> moimAndUsersInMoims = moimAndUserService
			.getMoimAndUsers(moimsInUser);
		return MoimResponseConverter.toMoimDtos(moimAndUsersInMoims);
	}

	@Transactional(readOnly = false)
	public Long modifyMoimName(MoimRequest.PatchMoimNameDto patchMoimNameDto, Long userId) {
		User user = userService.getUser(userId);
		Moim moim = moimService.getMoim(patchMoimNameDto.getMoimId());
		MoimAndUser moimAndUser = moimAndUserService.getMoimAndUser(moim, user);
		moimAndUser.updateCustomName(patchMoimNameDto.getMoimName());
		return moim.getId();
	}

	@Transactional(readOnly = false)
	public MoimResponse.MoimParticipantDto createMoimAndUser(Long userId, String code) {
		User user = userService.getUser(userId);
		Moim moim = moimService.getMoim(code);

		moimAndUserService.validateExistsMoimAndUser(moim, user);

		Integer numberOfMoimMembers = moimAndUserService.getMoimMemberSize(moim);
		MoimAndUser moimAndUser = MoimAndUserConverter
			.toMoimAndUser(moim.getName(), MOIM_USERS_COLOR[numberOfMoimMembers], user, moim);

		moimAndUserService.create(moimAndUser);
		return MoimResponseConverter.toMoimParticipantDto(moim);
	}

	@Transactional(readOnly = false)
	public void removeMoimAndUser(Long userId, Long moimId) {
		User user = userService.getUser(userId);
		Moim moim = moimService.getMoim(moimId);
		MoimAndUser moimAndUser = moimAndUserService.getMoimAndUser(moim, user);
		moimAndUserService.removeMoimAndUser(moimAndUser);
	}
}

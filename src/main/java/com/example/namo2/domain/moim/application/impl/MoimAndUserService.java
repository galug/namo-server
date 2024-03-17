package com.example.namo2.domain.moim.application.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.moim.dao.repository.MoimAndUserRepository;
import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimAndUser;

import com.example.namo2.domain.user.domain.User;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimAndUserService {
	private final MoimAndUserRepository moimAndUserRepository;

	public MoimAndUser create(MoimAndUser moimAndUser) {
		return moimAndUserRepository.save(moimAndUser);
	}

	public List<MoimAndUser> getMoimAndUsers(User user) {
		return moimAndUserRepository.findMoimAndUserByUser(user);
	}

	public List<MoimAndUser> getMoimAndUsers(Moim moim) {
		return moimAndUserRepository.findMoimAndUserByMoim(moim);
	}

	public List<MoimAndUser> getMoimAndUsers(List<Moim> moims) {
		return moimAndUserRepository.findMoimAndUserByMoim(moims);
	}

	public MoimAndUser getMoimAndUser(Moim moim, User user) {
		return moimAndUserRepository.findMoimAndUserByUserAndMoim(user, moim)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_AND_USER_FAILURE));
	}

	public Integer getMoimMemberSize(Moim moim) {
		return moimAndUserRepository.countMoimAndUserByMoim(moim);
	}

	public void validateExistsMoimAndUser(Moim moim, User user) {
		if (moimAndUserRepository.existsMoimAndUserByMoimAndUser(moim, user)) {
			throw new BaseException(BaseResponseStatus.DUPLICATE_PARTICIPATE_FAILURE);
		}
	}

	public void removeMoimAndUser(MoimAndUser moimAndUser) {
		moimAndUserRepository.delete(moimAndUser);
	}

	public void removeMoimAndUsersByUser(User user){
		moimAndUserRepository.deleteAllByUser(user);
	}
}

package com.example.namo2.domain.group.application.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.namo2.domain.group.dao.repository.schedule.MoimScheduleAlarmRepository;
import com.example.namo2.domain.group.dao.repository.schedule.MoimScheduleAndUserRepository;
import com.example.namo2.domain.group.domain.Moim;
import com.example.namo2.domain.group.domain.MoimAndUser;
import com.example.namo2.domain.group.domain.MoimSchedule;
import com.example.namo2.domain.group.domain.MoimScheduleAlarm;
import com.example.namo2.domain.group.domain.MoimScheduleAndUser;

import com.example.namo2.domain.user.domain.User;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimScheduleAndUserService {
	private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;
	private final MoimScheduleAlarmRepository moimScheduleAlarmRepository;

	public void createAll(List<MoimScheduleAndUser> moimScheduleAndUsers, Moim moim) {
		validateMoimScheduleAndUserSize(moimScheduleAndUsers);
		validateMoimScheduleAndUsersInMoimAndUsers(moimScheduleAndUsers, moim);
		moimScheduleAndUserRepository.saveAll(moimScheduleAndUsers);
	}

	private void validateMoimScheduleAndUserSize(List<MoimScheduleAndUser> moimScheduleAndUsers) {
		if (moimScheduleAndUsers.size() == 0) {
			throw new BaseException(BaseResponseStatus.EMPTY_USERS_FAILURE);
		}
	}

	private void validateMoimScheduleAndUsersInMoimAndUsers(List<MoimScheduleAndUser> moimScheduleAndUsers, Moim moim) {
		Set<User> moimUsers = moim.getMoimAndUsers()
			.stream()
			.map(MoimAndUser::getUser)
			.collect(Collectors.toSet());
		for (MoimScheduleAndUser moimScheduleAndUser : moimScheduleAndUsers) {
			if (!moimUsers.contains(moimScheduleAndUser.getUser())) {
				throw new BaseException(BaseResponseStatus.NOT_USERS_IN_MOIM);
			}

		}
	}

	public void removeMoimScheduleAndUser(MoimSchedule moimSchedule) {
		moimScheduleAndUserRepository.deleteMoimScheduleAndUserByMoimSchedule(moimSchedule);
	}

	public void removeMoimScheduleAndUserInPersonalSpace(MoimScheduleAndUser moimScheduleAndUser) {
		moimScheduleAndUser.handleDeletedPersonalSchedule();
	}

	public void removeMoimScheduleAndUsers(List<MoimScheduleAndUser> moimScheduleAndUsers) {
		moimScheduleAndUserRepository.deleteAll(moimScheduleAndUsers);
	}

	public MoimScheduleAndUser getMoimScheduleAndUser(MoimSchedule moimSchedule, User user) {
		return moimScheduleAndUserRepository.findMoimScheduleAndUserByMoimScheduleAndUser(moimSchedule, user)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE));
	}

	public List<MoimScheduleAndUser> getAllByUser(User user) {
		return moimScheduleAndUserRepository.findAllByUser(user);
	}

	public void removeMoimScheduleAlarm(MoimScheduleAndUser moimScheduleAndUser) {
		moimScheduleAlarmRepository.deleteMoimScheduleAlarmByMoimScheduleAndUser(moimScheduleAndUser);
	}

	public void removeMoimScheduleAlarm(List<MoimScheduleAndUser> moimScheduleAndUser) {
		moimScheduleAlarmRepository.deleteMoimScheduleAlarmByMoimScheduleAndUser(moimScheduleAndUser);
	}

	public void removeMoimScheduleAlarms(List<MoimScheduleAndUser> moimScheduleAndUsers) {
		moimScheduleAndUsers.forEach(moimScheduleAndUser ->
			moimScheduleAlarmRepository.deleteAll(moimScheduleAndUser.getMoimScheduleAlarms()));
	}

	public void createMoimScheduleAlarm(MoimScheduleAlarm moimScheduleAlarm) {
		moimScheduleAlarmRepository.save(moimScheduleAlarm);
	}

	/**
	 * 메서드 네이밍에 더 분명한 의미를 담기 위해서 규칙을 좀 더
	 * 자세히 세워보는 것도 괜찮을 것 같아요
	 */
	public List<MoimScheduleAndUser> getMoimScheduleAndUsersForMonthMoimMemo(User user, List<LocalDateTime> dates,
		Pageable page) {
		return moimScheduleAndUserRepository.findMoimScheduleMemoByMonthPaging(user, dates, page);
	}

	public void modifyText(MoimScheduleAndUser moimScheduleAndUser, String moimScheduleText) {
		moimScheduleAndUser.updateText(moimScheduleText);
	}

	public void removeMoimScheduleMemoInPersonalSpace(MoimScheduleAndUser moimScheduleAndUser) {
		moimScheduleAndUser.handleDeletedPersonalMoimMemo();
	}
}

package com.example.namo2.domain.moim.application.impl;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.namo2.domain.memo.MoimMemoRepository;
import com.example.namo2.domain.memo.MoimMemoService;
import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.moim.dao.repository.MoimRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleAndUserRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleRequest;
import com.example.namo2.domain.user.dao.repository.UserRepository;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimService {
	private final MoimRepository moimRepository;
	private final MoimScheduleRepository moimScheduleRepository;
	private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;
	private final MoimMemoRepository moimMemoRepository;
	private final UserRepository userRepository;

	/**
	 * 추후 MoimMemo Refactoring 작업에서 제거할 예정
	 */
	private final MoimMemoService moimMemoService;

	public Moim create(Moim moim) {
		return moimRepository.save(moim);
	}

	public Moim getMoim(Long moimId) {
		return moimRepository.findById(moimId)
				.orElseThrow(() -> new BaseException(NOT_FOUND_MOIM_FAILURE));
	}

	public Moim getMoim(String code) {
		return moimRepository.findMoimByCode(code)
				.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_FAILURE));
	}

	public void removeSchedule(Long moimScheduleId) {
		MoimSchedule moimSchedule = moimScheduleRepository.findById(moimScheduleId)
				.orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
		MoimMemo moimMemo = moimMemoRepository.findMoimMemoAndLocationsByMoimSchedule(moimSchedule);

		//         모임 메모가 있는 경우 모임 메모 장소를 모두 삭제 후 모임 메모 삭제
		if (moimMemo != null) {
			moimMemo.getMoimMemoLocations()
					.stream()
					.forEach((moimMemoLocation -> moimMemoService.removeMoimMemoLocation(moimMemoLocation.getId())));
			moimMemoRepository.delete(moimMemo);
		}

		moimScheduleAndUserRepository.deleteMoimScheduleAndUserByMoimSchedule(moimSchedule);
		moimScheduleRepository.delete(moimSchedule);
	}

	/**
	 * 추후 모임 메모 리팩토링 작업에서 제거 예정
	 */
	@Transactional(readOnly = false)
	public void createMoimScheduleText(Long moimScheduleId,
			Long userId,
			MoimScheduleRequest.PostMoimScheduleTextDto moimScheduleText) {
		MoimSchedule moimSchedule = moimScheduleRepository.findById(moimScheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
		MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserRepository.findMoimScheduleAndUserByMoimScheduleAndUser(moimSchedule, user).orElseThrow(() -> new BaseException(NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE));
		moimScheduleAndUser.updateText(moimScheduleText.getText());
	}
}

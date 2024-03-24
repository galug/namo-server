package com.example.namo2.domain.user.application.impl;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.user.dao.repository.TermRepository;
import com.example.namo2.domain.user.dao.repository.UserRepository;
import com.example.namo2.domain.user.domain.Term;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.user.domain.UserStatus;

import com.example.namo2.global.common.exception.BaseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final TermRepository termRepository;

	public User createUser(User user) {
		return userRepository.save(user);
	}

	public void checkEmailAndName(String email, String name) {
		if (email.isBlank() || name.isBlank()) {
			throw new BaseException(USER_POST_ERROR);
		}
	}

	public User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
	}

	public List<User> getUsers(List<Long> users) {
		return userRepository.findUsersById(users);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	public User getUserByRefreshToken(String refreshToken) {
		return userRepository.findUserByRefreshToken(refreshToken)
			.orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
	}

	public List<User> getInactiveUser() {
		return userRepository.findUsersByStatusAndDate(UserStatus.INACTIVE, LocalDateTime.now().minusDays(3));
	}

	public void updateRefreshToken(Long userId, String refreshToken) {
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
		user.updateRefreshToken(refreshToken);
	}

	public void createTerm(List<Term> terms) {
		for (Term term : terms) {
			if (!term.getIsCheck()) {
				throw new BaseException(NOT_CHECK_TERM_ERROR);
			}
			termRepository.findTermByContentAndUser(term.getContent(), term.getUser())
				.ifPresentOrElse(
					savedTerm -> savedTerm.update(),
					() -> termRepository.save(term)
				);
		}
	}

	/*
	 * User status 도입?
	 * 삭제 한다면..
	 * - 개인 스케줄 삭제
	 *  - 개인 스케줄 알람 삭제
	 *  - 개인 스케줄 이미지 삭제
	 * - 모임 스케줄 & 유저에서 삭제
	 * - 모임 스케줄 알람 삭제
	 * - 카테고리 삭제
	 * - 모임 메모 삭제
	 * - 모임 메모 로케이션 & 유저 삭제
	 * - 모임 & 유저에서 삭제
	 */
	public void removeUser(User user) {
		userRepository.delete(user);
	}
}

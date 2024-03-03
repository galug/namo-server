package com.example.namo2.domain.user.application.impl;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.user.dao.repository.UserRepository;
import com.example.namo2.domain.user.domain.User;

import com.example.namo2.global.common.exception.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User createUser(User user) {
		return userRepository.save(user);
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

	public void updateRefreshToken(Long userId, String refreshToken) {
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
		user.updateRefreshToken(refreshToken);
	}
}

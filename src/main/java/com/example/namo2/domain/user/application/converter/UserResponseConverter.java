package com.example.namo2.domain.user.application.converter;

import com.example.namo2.domain.user.ui.dto.UserResponse;

public class UserResponseConverter {
	private UserResponseConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static UserResponse.SignUpDto toSignUpDto(String accessToken, String refreshToken) {
		return UserResponse.SignUpDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}

package com.example.namo2.domain.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponse {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SignUpDto {
		private String accessToken;
		private String refreshToken;
	}
}

package com.example.namo2.domain.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponse {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class SignUpDto {
		private String accessToken;
		private String refreshToken;
	}
}

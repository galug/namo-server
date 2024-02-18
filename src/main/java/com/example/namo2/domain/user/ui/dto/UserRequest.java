package com.example.namo2.domain.user.ui.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequest {
	private UserRequest() {
		throw new IllegalStateException("Utility class");
	}

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class LogoutDto {
		@NotBlank
		private String accessToken;
	}

	@Setter
	@Getter
	@AllArgsConstructor
	public static class SignUpDto {
		@NotBlank
		private String accessToken;
		@NotBlank
		private String refreshToken;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SocialSignUpDto {
		@NotBlank
		String accessToken;
	}
}

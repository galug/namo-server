package com.example.namo2.global.feignClient.apple;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class AppleResponse {
	private AppleResponse() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@Setter
	public static class ApplePublicKeyDto {
		private String kty;
		private String kid;
		private String use;
		private String alg;
		private String n;
		private String e;
	}

	@Getter
	@Setter
	public static class ApplePublicKeyListDto {
		private List<ApplePublicKeyDto> keys;
	}

	@Getter
	@Setter
	public static class GetAccessToken{
		@JsonProperty("access_token")
		private String accessToken;
		@JsonProperty("access_token")
		private Long expiresIn;
		@JsonProperty("access_token")
		private String idToken;
		@JsonProperty("access_token")
		private String refreshToken;
		@JsonProperty("access_token")
		private String tokenType;
	}
}

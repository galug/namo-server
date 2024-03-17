package com.example.namo2.global.feignClient.kakao;

import lombok.Getter;

public class KakaoResponse {
	public KakaoResponse() {
		throw new RuntimeException("utility class");
	}

	@Getter
	public static class UnlinkDto {
		private Long id;
	}
}

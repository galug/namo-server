package com.example.namo2.global.feignclient.kakao;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoAuthClient {
	private final KakaoAuthApi kakaoAuthApi;

	public KakaoResponse.UnlinkDto unlinkKakao(String accessToken) {
		return kakaoAuthApi.unlinkKakao("Bearer " + accessToken);
	}
}

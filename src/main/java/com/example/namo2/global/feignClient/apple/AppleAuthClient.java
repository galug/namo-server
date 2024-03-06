package com.example.namo2.global.feignClient.apple;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleAuthClient {
	private final AppleAuthApi appleAuthApi;
	// private final AppleProperties appleProperties;

	public AppleResponse.ApplePublicKeyListDto getApplePublicKeys(){
		return appleAuthApi.getApplePublicKeys();
	}
}

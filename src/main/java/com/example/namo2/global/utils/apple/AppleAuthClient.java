package com.example.namo2.global.utils.apple;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleAuthClient {
	private final AppleAuthClient appleAuthClient;

	public AppleResponse.ApplePublicKeyListDto getApplePublicKeys(){
		return appleAuthClient.getApplePublicKeys();
	}
}

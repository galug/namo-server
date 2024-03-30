package com.example.namo2.global.feignclient.apple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleAuthClient {
	private final Logger logger = LoggerFactory.getLogger(AppleAuthClient.class);
	private final AppleAuthApi appleAuthApi;
	private final AppleProperties appleProperties;

	public AppleResponse.ApplePublicKeyListDto getApplePublicKeys() {
		return appleAuthApi.getApplePublicKeys();
	}

	public String getAppleToken(String clientSecret, String code) {
		logger.debug("{}", appleProperties.getClientId());
		AppleResponse.GetAccessToken getAccessToken = appleAuthApi.getAppleToken(
			appleProperties.getClientId(),
			clientSecret,
			code,
			"authorization_code",
			appleProperties.getRedirectUri()
		);

		logger.debug("getAppleToken : {}", getAccessToken.getAccessToken());
		return getAccessToken.getAccessToken();
	}

	public void revoke(String clientSecret, String token) {
		logger.debug("[client] token {}", token);
		appleAuthApi.revoke(
			appleProperties.getClientId(),
			clientSecret,
			token,
			"access_token"
		);
	}

}

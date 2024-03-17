package com.example.namo2.global.feignClient.naver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NaverAuthClient {
	private final Logger logger = LoggerFactory.getLogger(NaverAuthClient.class);
	private final NaverAuthApi naverAuthApi;
	private final NaverOpenApi naverOpenApi;
	private final NaverProperties naverProperties;

	public void unlinkNaver(String accessToken) {
		try {//URL인코딩
			accessToken = URLEncoder.encode(accessToken, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.debug("clientId = {}, clientSecret = {}", naverProperties.getClientId(),
			naverProperties.getClientSecret());

		NaverResponse.UnlinkDto dto = naverAuthApi.unlinkNaver(
			"delete",
			naverProperties.getClientId(),
			naverProperties.getClientSecret(),
			accessToken,
			"NAVER"
		);

		logger.debug("네이버 탈퇴 res : {}, {}", dto.getAccess_token(), dto.getResult());
	}

	public void tokenAvailability(String accessToken) {
		naverOpenApi.tokenAvailability(
			"Bearer " + accessToken
		);
	}

}

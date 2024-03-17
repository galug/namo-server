package com.example.namo2.global.feignClient.apple;

import feign.Response;
import feign.codec.ErrorDecoder;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

public class AppleFeignException implements ErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {
		if (response.status() >= 400 && response.status() <= 499) {
			return switch (response.status()) {
				case 400 -> new BaseException(BaseResponseStatus.APPLE_UNAUTHORIZED);
				default -> throw new IllegalStateException(
					"Unexpected value: " + response.status());
			};
		} else {
			return new BaseException(BaseResponseStatus.FEIGN_SERVER_ERROR);
		}
	}
}

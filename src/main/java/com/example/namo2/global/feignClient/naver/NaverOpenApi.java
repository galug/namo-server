package com.example.namo2.global.feignClient.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
	name = "naver-openapi-client",
	url = "https://openapi.naver.com/v1/nid/",
	configuration = NaverFeignConfiguration.class
)
public interface NaverOpenApi {
	@GetMapping("verify")
	NaverResponse.Availability tokenAvailability(
		@RequestHeader("Authorization") String authorization
	);
}

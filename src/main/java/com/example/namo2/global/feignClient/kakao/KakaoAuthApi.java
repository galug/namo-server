package com.example.namo2.global.feignClient.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
	name = "kakao-auth-client",
	url = "https://kapi.kakao.com/v1/user",
	configuration = KakaoFeignConfiguration.class
)
public interface KakaoAuthApi {
	@PostMapping(value = "/unlink")
	KakaoResponse.UnlinkDto unlinkKakao(@RequestHeader("Authorization") String authorization);
}

package com.example.namo2.global.utils.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
	name = "apple-public-key-client",
	url = "https://appleid.apple.com/auth",
	configuration = AppleFeignConfiguration.class
)
public interface AppleAuthApi {
	@GetMapping(value = "/keys", consumes = "application/x-www-form-urlencoded")
	AppleResponse.ApplePublicKeyListDto getApplePublicKeys();
}

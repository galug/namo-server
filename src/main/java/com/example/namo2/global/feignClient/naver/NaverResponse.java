package com.example.namo2.global.feignClient.naver;

import lombok.Getter;
import lombok.Setter;

public class NaverResponse {
	public NaverResponse(){
		throw new IllegalStateException("Utility class.");
	}

	@Getter
	public static class UnlinkDto{
		private String access_token;
		private String result;
	}

	@Getter
	public static class Availability{
		private String resultcode;
		private String message;
	}
}


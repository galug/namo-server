package com.example.namo2.global.utils.apple;

import org.springframework.context.annotation.Bean;

import feign.Logger;

public class AppleFeignConfiguration {
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}

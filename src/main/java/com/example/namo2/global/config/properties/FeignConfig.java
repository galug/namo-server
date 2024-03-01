package com.example.namo2.global.config.properties;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.example.namo2.global.utils.apple")
public class FeignConfig {
}

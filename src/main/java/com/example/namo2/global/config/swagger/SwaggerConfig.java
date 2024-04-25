package com.example.namo2.global.config.swagger;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SwaggerConfig {
	private static final String API_NAME = "Namo Api";
	private static final String API_VERSION = "0.0.1";
	private static final String API_DESCRIPTION = "나모 프로젝트 명세서 ";

	@Value("${swagger.server.url}")
	private String serverUrl;

	@Bean
	public OpenAPI getOpenAPI() {
		return new OpenAPI()
				.components(new Components())
				.servers(List.of(getServer()))
				.info(getInfo());
	}

	private Info getInfo() {
		return new Info()
				.title(API_NAME)
				.version(API_VERSION)
				.description(API_DESCRIPTION);
	}

	private Server getServer() {
		return new Server()
				.url(serverUrl)
				.description("dev server");
	}
}

package com.example.namo2.global.config.swagger;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SwaggerConfig {
	public static final String ACCESS_TOKEN = "accessToken";

	private static final String API_NAME = "Namo Api";
	private static final String API_VERSION = "0.0.1";
	private static final String API_DESCRIPTION = "나모 프로젝트 명세서 ";

	@Value("${swagger.server.url}")
	private String serverUrl;

	@Bean
	public OpenAPI getOpenAPI() {

		SecurityRequirement securityRequirement = new SecurityRequirement()
				.addList(ACCESS_TOKEN);

		return new OpenAPI()
				.components(getComponents())
				.servers(List.of(getServer()))
				.security(Collections.singletonList(securityRequirement))
				.info(getInfo());
	}

	private static Components getComponents() {
		SecurityScheme securityScheme = new SecurityScheme()
				.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
				.in(SecurityScheme.In.HEADER).name("Authorization");

		return new Components().addSecuritySchemes(ACCESS_TOKEN, securityScheme);
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

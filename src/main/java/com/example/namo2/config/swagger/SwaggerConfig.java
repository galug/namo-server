package com.example.namo2.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String API_NAME = "Namo Api";
    private static final String API_VERSION = "0.0.1";
    private static final String API_DESCRIPTION = "나모 프로젝트 명세서 ";

    @Bean
    public OpenAPI getOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(getInfo());
    }

    private Info getInfo() {
        return new Info()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION);
    }
}

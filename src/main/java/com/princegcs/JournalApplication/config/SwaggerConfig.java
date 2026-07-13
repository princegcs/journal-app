package com.princegcs.JournalApplication.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
public class SwaggerConfig {

    @Value("${app.server-url}")
    private String apiUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                        new Info().title("Journal Management API")
                                .version("1.0")
                                .description("Rest API for secure journal management with JWT authentication,"
                                        + " AI-powered sentiment analysis, Redis Caching, and Apache Kafka integration"))
                .servers(Arrays.asList(new Server().url(apiUrl).description("Current Environment"))
                ).addSecurityItem(
                        new SecurityRequirement().addList("Bearer Authentication")
                )
                .components(
                        new Components().addSecuritySchemes(
                                "Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT Token")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        )
                );
    }

}

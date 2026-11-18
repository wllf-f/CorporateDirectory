package com.wei.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("SptringBoot3")
                .version("1.0")
                .description("SpringBoot3Project")
                .contact(new Contact().name("wei").url("xxxxxxxxxx"))
        );
    }
}

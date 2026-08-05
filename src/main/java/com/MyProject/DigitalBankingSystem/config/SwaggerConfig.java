package com.MyProject.DigitalBankingSystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankingOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("Digital Banking System APIs")
                        .version("1.0")
                        .description("Secure Banking Backend using Spring Boot, JWT, Redis and MySQL")
                        .contact(
                                new Contact()
                                        .name("Amaan Coatwala")
                                        .email("amaancoatwala@gmail.com")
                        )

        );
    }

}

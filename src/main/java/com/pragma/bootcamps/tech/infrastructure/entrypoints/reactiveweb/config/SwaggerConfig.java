package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Technology Microservice",
                description = "Technologies Management API",
                version = "1.0.0"
        )
)
public class SwaggerConfig {
}

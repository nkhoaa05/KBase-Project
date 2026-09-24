package org.example.kbase.common.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
            title = "KBASE System APIs",
            version = "1.0",
            description = "REST APIs for KBASE System"
    )
)
public class OpenApiConfig {
}

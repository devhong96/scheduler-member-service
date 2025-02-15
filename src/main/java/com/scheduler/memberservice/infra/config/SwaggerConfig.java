package com.scheduler.memberservice.infra.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Member Service Api")
)

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

}

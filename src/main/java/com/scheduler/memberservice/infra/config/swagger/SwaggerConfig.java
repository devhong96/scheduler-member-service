package com.scheduler.memberservice.infra.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Member Service Api").version("1.0"))
                .addServersItem(new Server().url("/scheduler-member-service").description("API Gateway URL"))
                .addServersItem(new Server().url("/").description("Local DEV Server"));
    }

}

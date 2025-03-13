package com.scheduler.memberservice.testSet.testConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public WireMockServer wireMockServer() {
        return new WireMockServer(8080);
    }

    @Bean
    @Primary
    ApplicationEventPublisher publisher() {
        return mock(ApplicationEventPublisher.class);
    }

}

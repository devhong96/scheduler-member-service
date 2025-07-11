package com.scheduler.memberservice.infra.config.setting;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {

    public Long accessTokenPeriod;
    public Long refreshTokenPeriod;
}

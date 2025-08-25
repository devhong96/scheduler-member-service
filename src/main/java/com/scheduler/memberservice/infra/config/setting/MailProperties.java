package com.scheduler.memberservice.infra.config.setting;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
public class MailProperties {

    private String host;
    private int port;
    private String username;
    private String password;
    private Map<String, String> properties = new HashMap<>();
}

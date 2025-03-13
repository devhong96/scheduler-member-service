package com.scheduler.memberservice.infra.config.setting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import feign.okhttp.OkHttpClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static com.querydsl.jpa.JPQLTemplates.DEFAULT;

@Configuration
@RequiredArgsConstructor
public class QueryConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(DEFAULT, em);
    }

    @Bean
    public JavaMailSender mailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}

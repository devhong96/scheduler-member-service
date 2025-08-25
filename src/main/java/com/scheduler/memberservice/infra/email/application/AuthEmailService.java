package com.scheduler.memberservice.infra.email.application;

import com.scheduler.memberservice.infra.email.dto.EmailMessageDto;
import com.scheduler.memberservice.infra.email.event.SendEmailEvent;
import com.scheduler.memberservice.infra.exception.custom.InvalidAuthNumException;
import com.scheduler.memberservice.infra.redis.RedisVerifyCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.AuthCodeRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final RedisVerifyCache redisVerifyCache;
    private final ApplicationEventPublisher eventPublisher;

    public void sendUsername(String email, String username) {

        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .from(from)
                .to(email)
                .subject("아이디 찾기")
                .plain(FindUsernameHtml.plain(email, username))
                .html(FindUsernameHtml.html(email, username))
                .build();

        eventPublisher.publishEvent(new SendEmailEvent(emailMessageDto));
    }

    public void sendAuthNum(String email, String username) {

        StringBuilder authNumBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            authNumBuilder.append((int) (Math.random() * 10));
        }

        String authNum = authNumBuilder.toString();

        redisVerifyCache.saveAuthNum(email, authNum);

        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .from(from)
                .to(email)
                .subject("비밀번호 찾기")
                .plain(VerificationHtml.plain(authNum, "5"))
                .html(VerificationHtml.html(authNum, "5"))
                .build();

        eventPublisher.publishEvent(new SendEmailEvent(emailMessageDto));
    }

    public void verifyAuthCode(AuthCodeRequest authCodeRequest) {

        String email = authCodeRequest.getEmail();
        String authNum = authCodeRequest.getAuthNum();

        String authNumByEmail = redisVerifyCache.getAuthNumByEmail(email);

        if (!authNumByEmail.equals(authNum)) {
            throw new InvalidAuthNumException();
        }

        redisVerifyCache.invalidateAuthNum(email);
    }
}

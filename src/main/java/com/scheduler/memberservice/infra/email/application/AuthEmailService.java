package com.scheduler.memberservice.infra.email.application;

import com.scheduler.memberservice.infra.email.dto.EmailMessageDto;
import com.scheduler.memberservice.infra.email.event.SendEmailEvent;
import com.scheduler.memberservice.infra.exception.custom.InvalidAuthNumException;
import com.scheduler.memberservice.member.cache.VerifyCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.AuthCodeRequest;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class AuthEmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final VerifyCache verifyCache;

    private final ApplicationEventPublisher eventPublisher;

    public void sendUsername(String email, String username) {

        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .from(from)
                .to(email)
                .subject("")
                .message(email + "님의 아이디는 " + username + "입니다.")
                .build();

        eventPublisher.publishEvent(new SendEmailEvent(emailMessageDto));
    }

    public void sendAuthNum(String email, String username) {

        StringBuilder authNumBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            authNumBuilder.append((int) (Math.random() * 10));
        }

        String authNum = authNumBuilder.toString();

        verifyCache.saveDirectOrder(email, authNum);

        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .from(from)
                .to(email)
                .subject("")
                .message(username + "님의 인증번호는 " + authNum + " 입니다")
                .build();

        log.info(emailMessageDto.toString());

        eventPublisher.publishEvent(new SendEmailEvent(emailMessageDto));
    }

    public void verifyAuthCode(AuthCodeRequest authCodeRequest) {

        String email = authCodeRequest.getEmail();
        String authNum = authCodeRequest.getAuthNum();

        String authNumByEmail = verifyCache.getAuthNumByEmail(email);

        if (!authNumByEmail.equals(authNum)) {
            throw new InvalidAuthNumException();
        }

        verifyCache.invalidateAuthNum(email);
    }
}

package com.scheduler.memberservice.infra.email.dto;

import com.scheduler.memberservice.infra.email.event.SendAuthNumEvent;
import com.scheduler.memberservice.infra.email.event.SendEmailEvent;
import com.scheduler.memberservice.infra.exception.custom.InvalidAuthNumException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.AuthNumDto;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class HtmlEmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final ConcurrentHashMap<String, AuthCode> authNumMap = new ConcurrentHashMap<>();
    private final long AUTH_NUM_EXPIRY_MINUTES = 5; // 인증 번호 유효 시간 (초 단위)
    private final long MINUTES = 60 * 1000;

    private final ApplicationEventPublisher eventPublisher;

    public void sendAuthNum(String email, String username) {

        StringBuilder authNumBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            authNumBuilder.append((int) (Math.random() * 10));
        }

        String authNum = authNumBuilder.toString();

        authNumMap.put(email, new AuthCode(authNum, System.currentTimeMillis()));

        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .from(from)
                .to(email)
                .subject("")
                .message(username + "님의 인증번호는 " + authNum + " 입니다")
                .build();

        log.info(emailMessageDto.toString());

        eventPublisher.publishEvent(new SendAuthNumEvent(emailMessageDto));

    }


    public boolean confirmAuthNum(AuthNumDto authNumDto) {

        String email = authNumDto.getEmail();
        String authNum = authNumDto.getAuthNum();

        AuthCode code = authNumMap.get(email);

        if (code == null) {
            throw new InvalidAuthNumException();
        }
        if (!code.getAuthNum().equals(authNum)) {
            throw new InvalidAuthNumException();
        }
        if (System.currentTimeMillis() - code.getCreatedAt() > AUTH_NUM_EXPIRY_MINUTES * MINUTES) {
            authNumMap.remove(email);
            throw new InvalidAuthNumException();
        }

        authNumMap.remove(email);
        return true;
    }

    @Scheduled(fixedRate = AUTH_NUM_EXPIRY_MINUTES * MINUTES)
    protected void removeExpiredAuthNums() {
        long now = System.currentTimeMillis();
        for (String email : authNumMap.keySet()) {
            AuthCode entry = authNumMap.get(email);
            if (entry != null && (now - entry.getCreatedAt() > AUTH_NUM_EXPIRY_MINUTES * MINUTES)) {
                authNumMap.remove(email);
            }
        }
    }

    public void sendUsername(String email, String username) {

        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .from(from)
                .to(email)
                .subject("")
                .message(username)
                .build();

        eventPublisher.publishEvent(new SendEmailEvent(emailMessageDto));
    }

    // 인증 번호와 생성 시간을 함께 저장하는 클래스
    @Getter
    private static class AuthCode {
        private final String authNum;
        private final long createdAt;

        public AuthCode(String authNum, long createdAt) {
            this.authNum = authNum;
            this.createdAt = createdAt;
        }
    }
}

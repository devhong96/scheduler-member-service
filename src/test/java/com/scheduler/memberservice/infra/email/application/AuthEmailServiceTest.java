package com.scheduler.memberservice.infra.email.application;

import com.scheduler.memberservice.infra.email.event.SendEmailEvent;
import com.scheduler.memberservice.infra.redis.RedisVerifyCache;
import com.scheduler.memberservice.testSet.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.AuthCodeRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
class AuthEmailServiceTest {

    @Autowired
    private AuthEmailService authEmailService;

    @MockitoSpyBean
    private RedisVerifyCache redisVerifyCache;

    @MockitoBean
    private ApplicationEventPublisher eventPublisher;

    private static final String testEmail = "test@example.com";
    private static final String testUsername = "tester";

    @Test
    @DisplayName("인증번호 전송")
    void sendAuthNum() {

        authEmailService.sendAuthNum(testEmail, testUsername);

        // then
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> authNumCaptor = ArgumentCaptor.forClass(String.class);

        // 저장이 되었는지
        verify(redisVerifyCache)
                .saveAuthNum(emailCaptor.capture(), authNumCaptor.capture());

        // 인증번호가 6자리 숫자로 생성되었는지
        assertThat(authNumCaptor.getValue())
                .matches("\\d{6}");

        //이벤트가 발행 되었는지
        ArgumentCaptor<SendEmailEvent> eventCaptor = ArgumentCaptor.forClass(SendEmailEvent.class);
        verify(eventPublisher, times(1))
                .publishEvent(eventCaptor.capture());
    }

    @Test
    @DisplayName("아이디 전송")
    void sendUsername() {

        authEmailService.sendUsername(testEmail, testUsername);

        //이벤트가 발행 되었는지
        ArgumentCaptor<SendEmailEvent> eventCaptor = ArgumentCaptor.forClass(SendEmailEvent.class);

        verify(eventPublisher, times(1))
                .publishEvent(eventCaptor.capture());
    }

    @Test
    @DisplayName("인증번호 확인")
    void verifyAuthCode() {

        authEmailService.sendAuthNum(testEmail, testUsername);

        ArgumentCaptor<SendEmailEvent> eventCaptor = ArgumentCaptor.forClass(SendEmailEvent.class);

        verify(eventPublisher, times(1))
                .publishEvent(eventCaptor.capture());

        String message = eventCaptor.getValue().getMessage();
        String extractedAuthNum = extractAuthNumFromMessage(message);

        authEmailService.verifyAuthCode(new AuthCodeRequest(testEmail, extractedAuthNum));

        verify(redisVerifyCache, times(1)).invalidateAuthNum(testEmail);

    }

    private String extractAuthNumFromMessage(String message) {
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(message);
        return matcher.find() ? matcher.group() : null;  // 6자리 인증번호 반환
    }

}
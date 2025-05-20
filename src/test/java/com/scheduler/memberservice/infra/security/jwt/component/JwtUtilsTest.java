package com.scheduler.memberservice.infra.security.jwt.component;

import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.student.WithStudent;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.scheduler.memberservice.testSet.TestConstants.TEST_STUDENT_USERNAME;
import static com.scheduler.memberservice.testSet.TestConstants.TEST_TEACHER_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@IntegrationTest
class JwtUtilsTest {

    @Mock
    private Clock clock;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("토큰 생성 및 정보 확인")
    @WithStudent(username = TEST_STUDENT_USERNAME)
    void generateToken() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authentication);

        String username = Jwts.parser()
                .verifyWith(jwtUtils.getSigningKey())
                .build()
                .parseSignedClaims(jwtTokenDto.getAccessToken())
                .getPayload()
                .getSubject();

        assertThat(username).isEqualTo(TEST_STUDENT_USERNAME);
    }

//    @Test
    @WithStudent(username = TEST_STUDENT_USERNAME)
    void isExpired() {
        String expiredToken = "";

        assertThrows(ExpiredJwtException.class,
                () -> jwtUtils.isExpired(expiredToken));
    }

    @Test
    @WithStudent(username = TEST_STUDENT_USERNAME)
    @DisplayName("카테고리 확인")
    void getCategory() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authentication);

        String accessToken = jwtTokenDto.getAccessToken();
        String category = jwtUtils.getCategory(accessToken);

        assertThat("access").isEqualTo(category);
    }

    @Test
    @DisplayName("토큰으로 권한 확인")
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void getAuthentication() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authentication);

        Claims claims = Jwts.parser()
                .verifyWith(jwtUtils.getSigningKey())
                .build()
                .parseSignedClaims(jwtTokenDto.getAccessToken())
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo(TEST_TEACHER_USERNAME);
        assertThat(claims.get("auth")).isEqualTo("TEACHER");
    }

//    @Test
    @WithStudent(username = TEST_STUDENT_USERNAME)
    void isRefreshTokenExpiringSoon() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String refreshToken = jwtUtils.generateToken(authentication).getRefreshToken();

        Clock mockClock = mock(Clock.class);
        when(mockClock.instant()).thenReturn(Instant.now().plus(60L * 60L * 24L * 30L * 2, ChronoUnit.SECONDS));

        boolean refreshTokenExpiringSoon = jwtUtils.isRefreshTokenExpiringSoon(refreshToken);

        assertThat(refreshTokenExpiringSoon).isFalse();
    }

    @Test
    void deleteExpiredTokens() {

        jwtUtils.deleteExpiredTokens();

    }
}
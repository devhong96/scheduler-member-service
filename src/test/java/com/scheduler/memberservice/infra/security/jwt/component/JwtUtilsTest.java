package com.scheduler.memberservice.infra.security.jwt.component;

import com.scheduler.memberservice.infra.IntegrationTest;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.infra.teacher.WithTeacher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.scheduler.memberservice.infra.TestConstants.TEST_TEACHER_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@IntegrationTest
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("토큰 생성 및 정보 확인")
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void generateToken() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authentication);

        String username = Jwts.parser()
                .verifyWith(jwtUtils.getSigningKey())
                .build()
                .parseSignedClaims(jwtTokenDto.getAccessToken())
                .getPayload()
                .getSubject();

        assertThat(username).isEqualTo(TEST_TEACHER_USERNAME);
    }

//    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void isExpired() {
        String expiredToken = "";

        assertThrows(ExpiredJwtException.class,
                () -> jwtUtils.isExpired(expiredToken));
    }

//    @Test
    void getCategory() {
        String accessToken = "";
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
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void isRefreshTokenExpiringSoon() {
        String mockRefreshToken = "mock.refresh.tokenasfdasdf";

        when(jwtUtils.isRefreshTokenExpiringSoon(mockRefreshToken))
                .thenReturn(true);

        boolean refreshTokenExpiringSoon = jwtUtils.isRefreshTokenExpiringSoon(mockRefreshToken);
        assertThat(refreshTokenExpiringSoon).isTrue();
    }

    @Test
    void deleteExpiredTokens() {

        jwtUtils.deleteExpiredTokens();

    }
}
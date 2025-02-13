package com.scheduler.memberservice.infra.security.jwt.component;

import com.scheduler.memberservice.infra.security.jwt.RefreshTokenJpaRepository;
import com.scheduler.memberservice.infra.security.jwt.domain.RefreshToken;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtUtils jwtUtils;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    public Map<String, Object> refreshToken(String refresh,
                                            HttpServletResponse response) {

        Map<String, Object> responseMap = new HashMap<>();

        if (refresh == null) {
            responseMap.put("message", "Refresh token is null");
            responseMap.put("status", BAD_REQUEST);
            return responseMap;
        }

        if (!refresh.startsWith("Bearer ")) {
            responseMap.put("message", "Refresh token is invalid");
            responseMap.put("status", SC_UNAUTHORIZED);
            return responseMap;
        }

        refresh = refresh.replaceFirst("Bearer ", "");

        try {
            jwtUtils.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            responseMap.put("message", "Refresh token expired");
            responseMap.put("status", BAD_REQUEST);
            return responseMap;
        }

        String category = jwtUtils.getCategory(refresh);
        if (!"refresh".equals(category)) {
            responseMap.put("message", "Invalid refresh token");
            responseMap.put("status", BAD_REQUEST);
            return responseMap;
        }

        if (!refreshTokenJpaRepository.existsByRefreshToken(refresh)) {
            responseMap.put("message", "Invalid refresh token");
            responseMap.put("status", BAD_REQUEST);
            return responseMap;
        }

        Authentication authentication = jwtUtils.getAuthentication(refresh);

        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authentication);

        String accessToken = jwtTokenDto.getAccessToken();
        String refreshToken = jwtTokenDto.getRefreshToken();
        Date expiresDate = jwtTokenDto.getExpiresDate();

        response.setHeader("Authorization", "Bearer " + accessToken);
        responseMap.put("access", "Bearer " + accessToken);
        responseMap.put("status", OK);

        if (jwtUtils.isRefreshTokenExpiringSoon(refresh)) {
            refreshTokenJpaRepository.deleteByRefreshToken(refresh);
            response.setHeader("refresh", "Bearer " + refreshToken);
            responseMap.put("refresh", "Bearer " + refreshToken);
            refreshTokenJpaRepository.save(new RefreshToken(authentication.getName(), refreshToken, expiresDate));
        }

        return responseMap;
    }
}

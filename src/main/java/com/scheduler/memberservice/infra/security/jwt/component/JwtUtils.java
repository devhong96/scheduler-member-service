package com.scheduler.memberservice.infra.security.jwt.component;

import com.scheduler.memberservice.infra.security.jwt.RefreshTokenJpaRepository;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static io.jsonwebtoken.io.Decoders.BASE64;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private static final Long accessTokenPeriod = 60L * 60L * 12L; // 12시간
    private static final Long refreshTokenPeriod = 60L * 60L * 24L * 30L; // 24시간
    private static final long EXPIRATION_THRESHOLD = 60L * 60L;

    @Value("${custom.jwt.secretKey}")
    private String secretKey;

    private static SecretKey signingKey;

    @PostConstruct
    public void createSigningKey() {
        byte[] keyBytes = BASE64.decode(secretKey);
        signingKey =  Keys.hmacShaKeyFor(keyBytes);
    }

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Operation(summary = "토큰 생성", description = "계정 이름과 권한을 토큰에 저장")
    public JwtTokenDto generateToken(Authentication authentication) {

        String username = authentication.getName();
        String auth = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","))
                .substring(5);

        return new JwtTokenDto(
                generateToken(username, auth, "access", accessTokenPeriod),
                generateToken(username, auth, "refresh", refreshTokenPeriod),
                Date.from(Instant.now().plusSeconds(refreshTokenPeriod))
        );
    }

    private String generateToken(String username, String auth, String category, long tokenPeriod) {
        return Jwts.builder()
                .subject(username)
                .claim("auth", auth)
                .claim("category", category)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(tokenPeriod)))
                .signWith(signingKey)
                .compact();
    }

    @Operation(summary = "만료 확인")
    public void isExpired(String token) {
        getPayload(token).getExpiration();
    }

    @Operation(summary = "카테고리 확인")
    public String getCategory(String token) {
        return getPayload(token).get("category", String.class);
    }

    @Operation(summary = "인증")
    public Authentication getAuthentication(String token) {
        Claims claims = getPayload(token);

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    @Operation(summary = "리프레시 만료 확인")
    public boolean isRefreshTokenExpiringSoon(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expirationDate = claims.getExpiration();
        long currentTimeInMillis = System.currentTimeMillis();
        long timeUntilExpirationInSeconds = (expirationDate.getTime() - currentTimeInMillis) / 1000;

        return timeUntilExpirationInSeconds < EXPIRATION_THRESHOLD;
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return getPayload(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    @Scheduled(fixedRate = 3600_000 * 24)
    public void deleteExpiredTokens() {
        refreshTokenJpaRepository.deleteByExpiryDateBefore(Date.from(Instant.now()));
        log.info("Deleted expired tokens at {}", Date.from(Instant.now()));
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

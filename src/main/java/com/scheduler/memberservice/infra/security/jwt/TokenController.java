package com.scheduler.memberservice.infra.security.jwt;

import com.scheduler.memberservice.infra.exception.custom.TokenException;
import com.scheduler.memberservice.infra.security.jwt.component.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.scheduler.memberservice.infra.exception.ErrorCode.TOKEN_EXPIRED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("expired")
    private ResponseEntity<String> auth() {
        throw new TokenException(TOKEN_EXPIRED);
    }

    @PostMapping("reissue")
    private ResponseEntity<Map<String, Object>> refreshAuth(
            @CookieValue(value = "refresh") String refresh,
            HttpServletResponse response
    ) {
        return new ResponseEntity<>(refreshTokenService.refreshToken(refresh, response), OK);
    }
}


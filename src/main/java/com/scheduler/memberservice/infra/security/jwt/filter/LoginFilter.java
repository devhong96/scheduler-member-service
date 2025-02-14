package com.scheduler.memberservice.infra.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.memberservice.infra.security.base.AdminDetails;
import com.scheduler.memberservice.infra.security.base.TeacherDetails;
import com.scheduler.memberservice.infra.security.jwt.RefreshTokenJpaRepository;
import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.domain.RefreshToken;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.infra.security.jwt.dto.UsernamePasswordAutoDto;
import com.scheduler.memberservice.member.admin.domain.Admin;
import com.scheduler.memberservice.member.common.RoleType;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response
    ) throws AuthenticationException {

        UsernamePasswordAutoDto usernamePasswordAuthDto;

        try {
            usernamePasswordAuthDto = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernamePasswordAutoDto.class);

        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }

        String username = usernamePasswordAuthDto.getUsername();
        String password = usernamePasswordAuthDto.getPassword();

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {

        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authResult);
        Map<String, String> jsonResponse = new HashMap<>();

        String refreshTokenValue = jwtTokenDto.getRefreshToken();
        Date expiresDate = jwtTokenDto.getExpiresDate();

        if (authResult.getPrincipal() instanceof TeacherDetails) {

            Teacher teacher = ((TeacherDetails) authResult.getPrincipal()).getTeacher();
            String teacherId = teacher.getTeacherId();

            RefreshToken refreshToken = refreshTokenJpaRepository
                    .findRefreshTokenByUserId(teacherId)
                    .orElseGet(() -> refreshTokenJpaRepository.save(new RefreshToken(teacherId, refreshTokenValue, expiresDate)));

            jsonResponse = jsonProperty(teacher.getUsername(), teacher.getRoleType(),
                    jwtTokenDto.getAccessToken(), refreshToken.getRefreshToken()
            );
        }

        if (authResult.getPrincipal() instanceof AdminDetails) {

            Admin admin = ((AdminDetails) authResult.getPrincipal()).getAdmin();
            String adminId = admin.getAdminId();

            RefreshToken refreshToken = refreshTokenJpaRepository
                    .findRefreshTokenByUserId(adminId)
                    .orElseGet(() -> refreshTokenJpaRepository.save(new RefreshToken(adminId, refreshTokenValue, expiresDate)));

            jsonResponse = jsonProperty(admin.getUsername(), admin.getRoleType(),
                    jwtTokenDto.getAccessToken(), refreshToken.getRefreshToken()
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(jsonResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException exception
    ) throws IOException {

        String errorMessage;
        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "등록되지 않은 아이디입니다";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "아이디가 존재하지 않습니다. 아이디를 확인해주세요";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "관리자의 승인을 기다려주세요";
        } else {
            errorMessage = "관리자에게 문의해주세요";
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(SC_UNAUTHORIZED);
        response.getWriter().write(errorMessage);
    }

    private Map<String, String> jsonProperty(String username, RoleType roleType,
                                             String accessToken, String RefreshToken) {
        Map<String, String> jsonResponse = new HashMap<>();

        jsonResponse.put("username", username);
        jsonResponse.put("role", String.valueOf(roleType));
        jsonResponse.put("access", accessToken);
        jsonResponse.put("refresh", RefreshToken);

        return jsonResponse;
    }
}

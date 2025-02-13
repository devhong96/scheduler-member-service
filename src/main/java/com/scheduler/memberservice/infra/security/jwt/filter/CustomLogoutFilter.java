package com.scheduler.memberservice.infra.security.jwt.filter;

import com.scheduler.memberservice.infra.security.jwt.RefreshTokenJpaRepository;
import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtils jwtUtils;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        if (!request.getRequestURI().matches("^/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = request.getHeader("Authorization");

        String category = jwtUtils.getCategory(refreshToken);

        if (category == null || !category.equals("refresh")) {
            response.getWriter().write("not refreshToken or category is null");
            response.setStatus(SC_BAD_REQUEST);
            return;
        }

        try {
            jwtUtils.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            refreshTokenJpaRepository.deleteByRefreshToken(refreshToken);
            response.setStatus(SC_OK);
            return;
        }

        boolean isExist = refreshTokenJpaRepository
                .existsByRefreshToken(refreshToken);
        
        if (!isExist) {
            response.setStatus(SC_BAD_REQUEST);
            return;
        }

        refreshTokenJpaRepository.deleteByRefreshToken(refreshToken);
        response.setStatus(SC_OK);
    }
}

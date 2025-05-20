package com.scheduler.memberservice.infra.security;

import com.scheduler.memberservice.infra.security.jwt.RefreshTokenJpaRepository;
import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.filter.CustomLogoutFilter;
import com.scheduler.memberservice.infra.security.jwt.filter.JwtAuthFilter;
import com.scheduler.memberservice.infra.security.jwt.filter.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static final String[] INTERNAL_ENDPOINTS = {
            "/feign-member/**"
    };

    public static final String[] ADMIN_RESTRICTED_ENDPOINTS = {
            "/admin/**"
    };

    public static final String[] AUTHORIZED_ENDPOINTS = {
            "/manage/**"
    };

    public static final String[] TEACHER_ENDPOINTS = {
            "/teacher/manage/*",
    };

    public static final String[] STUDENT_ENDPOINTS = {
            "/student/**"
    };

    public static final String[] ENDPOINTS_WHITELISTS = {
//            "/**"
            "/member-api/**",
            "/actuator/**",
            "/login",
            "/help/*",
            "/student/join",
            "/teacher/find/*",
            "/teacher/join",
            "/token/*"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthFilter(jwtUtils), LoginFilter.class)
                .addFilterAt(
                        new LoginFilter(
                                jwtUtils, authenticationManager(authenticationConfiguration), refreshTokenJpaRepository),
                        UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(new CustomLogoutFilter(jwtUtils, refreshTokenJpaRepository), LogoutFilter.class)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(INTERNAL_ENDPOINTS).authenticated()
                                .requestMatchers(ENDPOINTS_WHITELISTS).permitAll()
                                .requestMatchers(STUDENT_ENDPOINTS).hasAuthority("STUDENT")
                                .requestMatchers(TEACHER_ENDPOINTS).hasAuthority("TEACHER")
                                .requestMatchers(ADMIN_RESTRICTED_ENDPOINTS).hasAuthority("ADMIN")
                                .requestMatchers(AUTHORIZED_ENDPOINTS).hasAnyAuthority("ADMIN", "TEACHER")
                                .anyRequest().denyAll()
                )
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(STATELESS));

        return httpSecurity.build();
    }
}

package com.scheduler.memberservice.infra;

import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.member.admin.domain.Admin;
import com.scheduler.memberservice.member.admin.repository.AdminJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@Slf4j
@RequiredArgsConstructor
public class WithAdminSecurityContextFactory implements WithSecurityContextFactory<WithAdmin> {

    private final PasswordEncoder passwordEncoder;
    private final AdminJpaRepository adminJpaRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    public SecurityContext createSecurityContext(WithAdmin withAdmin) {

        String username = withAdmin.username();
        String email = withAdmin.email();

        validateAdminAccount(username, email);

        UserDetails principal = userDetailsService.loadUserByUsername(username);

        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(
                new UsernamePasswordAuthenticationToken(
                        principal,
                        principal.getPassword(),
                        principal.getAuthorities()));

        final Authentication tokenAuthentication = new UsernamePasswordAuthenticationToken(
                principal,
                jwtTokenDto.getAccessToken(),
                principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(tokenAuthentication);

        log.info("Admin-login");

        return context;
    }

    private void validateAdminAccount(String username, String email) {
        adminJpaRepository.findByUsernameIs(username)
                .orElseGet(() -> adminJpaRepository
                        .save(Admin.create(username, "testPassword", email, passwordEncoder)));
    }
}

package com.scheduler.memberservice.infra.admin;

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

import static com.scheduler.memberservice.infra.TestConstants.TEST_ADMIN_EMAIL;
import static com.scheduler.memberservice.infra.TestConstants.TEST_ADMIN_PASSWORD;

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

        validateAdminAccount(username);

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

    private void validateAdminAccount(String username) {
        adminJpaRepository.findByUsernameIs(username)
                .orElseGet(() -> adminJpaRepository
                        .save(Admin.create(username, TEST_ADMIN_PASSWORD, TEST_ADMIN_EMAIL, passwordEncoder)));
    }
}

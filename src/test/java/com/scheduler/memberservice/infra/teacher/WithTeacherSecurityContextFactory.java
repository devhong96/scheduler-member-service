package com.scheduler.memberservice.infra.teacher;

import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
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

import static com.scheduler.memberservice.infra.TestConstants.*;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.JoinTeacherRequest;

@Slf4j
@RequiredArgsConstructor
public class WithTeacherSecurityContextFactory implements WithSecurityContextFactory<WithTeacher> {

    private final PasswordEncoder passwordEncoder;
    private final TeacherJpaRepository teacherJpaRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    public SecurityContext createSecurityContext(WithTeacher withTeacher) {

        String username = withTeacher.username();

        validateTeacherAccount(username);

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

    private void validateTeacherAccount(String username) {
        JoinTeacherRequest joinTeacherRequest = new JoinTeacherRequest();
        joinTeacherRequest.setUsername(username);
        joinTeacherRequest.setEmail(TEST_TEACHER_EMAIL);
        joinTeacherRequest.setPassword(TEST_TEACHER_PASSWORD);
        joinTeacherRequest.setTeacherName(TEST_TEACHER_NAME);

        teacherJpaRepository.findByUsernameIs(username)
                .orElseGet(() -> teacherJpaRepository
                        .save(Teacher.create(joinTeacherRequest, passwordEncoder)));
    }
}

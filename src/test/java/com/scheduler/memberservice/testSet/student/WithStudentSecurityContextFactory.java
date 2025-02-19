package com.scheduler.memberservice.testSet.student;

import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
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

import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static com.scheduler.memberservice.testSet.TestConstants.*;

@Slf4j
@RequiredArgsConstructor
public class WithStudentSecurityContextFactory implements WithSecurityContextFactory<WithStudent> {

    private final PasswordEncoder passwordEncoder;
    private final StudentJpaRepository studentJpaRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    public SecurityContext createSecurityContext(WithStudent withStudent) {

        String username = withStudent.username();

        validateStudentAccount(username);

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

        log.info("student-login");

        return context;
    }

    private void validateStudentAccount(String username) {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setUsername(username);
        request.setPassword(TEST_STUDENT_PASSWORD);
        request.setStudentName(TEST_STUDENT_NAME);
        request.setStudentEmail(TEST_STUDENT_EMAIL);
        request.setStudentPhoneNumber(TEST_STUDENT_PHONE_NUMBER);
        request.setStudentAddress(TEST_STUDENT_ADDRESS);
        request.setStudentDetailedAddress(TEST_STUDENT_DETAILED_ADDRESS);
        request.setStudentParentPhoneNumber(TEST_STUDENT_PARENT_PHONE_NUMBER);

        studentJpaRepository.findStudentByUsernameIs(username)
                .orElseGet(() -> studentJpaRepository
                        .save(Student.create(request, "testTeacherId", passwordEncoder)));
    }
}

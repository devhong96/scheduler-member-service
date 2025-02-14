package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.infra.IntegrationTest;
import com.scheduler.memberservice.infra.email.dto.AuthEmailService;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.teacher.WithTeacher;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.scheduler.memberservice.infra.TestConstants.*;
import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindPasswordRequest;
import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindUsernameRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.PwdEditRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@IntegrationTest
class TeacherCertServiceTest {

    @Autowired
    private TeacherCertService teacherCertService;

    @Autowired
    private TeacherJpaRepository teacherJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AuthEmailService authEmailService;

//    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void findUsernameByEmail() {
         FindUsernameRequest findUsernameRequest = new FindUsernameRequest();
         findUsernameRequest.setEmail(TEST_TEACHER_EMAIL);

         teacherCertService.findUsernameByEmail(findUsernameRequest);

    }

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("교사 비밀번호 변경")
    void initializePassword() {

        PwdEditRequest pwdEditRequest = new PwdEditRequest();
        pwdEditRequest.setNewPassword(TEST_TEACHER_NEW_PASSWORD);
        pwdEditRequest.setCheckPassword(TEST_TEACHER_NEW_PASSWORD);

        teacherCertService.initializePassword(pwdEditRequest);

        Teacher teacher = teacherJpaRepository.findByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        String newPassword = teacher.getPassword();

        boolean matches = passwordEncoder.matches(TEST_TEACHER_NEW_PASSWORD, newPassword);

        assertTrue(matches);
    }

    @Test
    @DisplayName("이메일 전송")
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void sendPasswordResetEmail() {
        FindPasswordRequest findInfoRequest = new FindPasswordRequest();
        findInfoRequest.setUsername(TEST_TEACHER_USERNAME);
        findInfoRequest.setEmail(TEST_TEACHER_EMAIL);

        teacherCertService.sendPasswordResetEmail(findInfoRequest);

        verify(authEmailService)
                .sendAuthNum(TEST_TEACHER_EMAIL, TEST_TEACHER_USERNAME);
    }

//    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void verifyAuthCode() {

        FindPasswordRequest findInfoRequest = new FindPasswordRequest();
        findInfoRequest.setUsername(TEST_TEACHER_USERNAME);
        findInfoRequest.setEmail(TEST_TEACHER_EMAIL);

        teacherCertService.sendPasswordResetEmail(findInfoRequest);

    }

    @Test
    @DisplayName("교사 이메일 변경")
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void changeUserEmail() {
        EditEmailRequest editEmailRequest = new EditEmailRequest();
        editEmailRequest.setEmail(TEST_NEW_TEACHER_EMAIL);

        teacherCertService.changeUserEmail(editEmailRequest);

        Teacher teacher = teacherJpaRepository.findByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        assertEquals(TEST_NEW_TEACHER_EMAIL, teacher.getEmail());
    }
}
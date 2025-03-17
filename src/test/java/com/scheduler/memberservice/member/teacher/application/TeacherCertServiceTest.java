package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.infra.email.application.AuthEmailService;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindPasswordRequest;
import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindUsernameRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.*;
import static com.scheduler.memberservice.testSet.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
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

    @Test
    @DisplayName("교사 회원 가입")
    void joinTeacher() {

        JoinTeacherRequest joinTeacherRequest = new JoinTeacherRequest();
        joinTeacherRequest.setUsername(TEST_TEACHER_USERNAME);
        joinTeacherRequest.setPassword(TEST_TEACHER_PASSWORD);
        joinTeacherRequest.setEmail(TEST_TEACHER_EMAIL);
        joinTeacherRequest.setTeacherName(TEST_TEACHER_NAME);

        teacherCertService.joinTeacher(joinTeacherRequest);

        Teacher teacher = teacherJpaRepository
                .findTeacherByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        assertNotNull(teacher);
    }

    @Test
    @DisplayName("이메일로 교사 아이디 찾기")
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void findUsernameByEmail() {

         FindUsernameRequest findUsernameRequest = new FindUsernameRequest();
         findUsernameRequest.setEmail(TEST_TEACHER_EMAIL);

         teacherCertService.findUsernameByEmail(findUsernameRequest);

        verify(authEmailService, times(1))
                .sendUsername(TEST_TEACHER_USERNAME, TEST_TEACHER_EMAIL);

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

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("교사 비밀번호 변경")
    void initializePassword() {

        PwdEditRequest pwdEditRequest = new PwdEditRequest();
        pwdEditRequest.setNewPassword(TEST_TEACHER_NEW_PASSWORD);
        pwdEditRequest.setCheckPassword(TEST_TEACHER_NEW_PASSWORD);

        teacherCertService.initializePassword(pwdEditRequest);

        Teacher teacher = teacherJpaRepository.findTeacherByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        String newPassword = teacher.getPassword();

        boolean matches = passwordEncoder.matches(TEST_TEACHER_NEW_PASSWORD, newPassword);

        assertTrue(matches);
    }

    @Test
    @DisplayName("교사 이메일 변경")
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void changeTeacherEmail() {
        EditEmailRequest editEmailRequest = new EditEmailRequest();
        editEmailRequest.setEmail(TEST_NEW_TEACHER_EMAIL);

        teacherCertService.changeTeacherEmail(editEmailRequest);

        Teacher teacher = teacherJpaRepository.findTeacherByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        assertEquals(TEST_NEW_TEACHER_EMAIL, teacher.getEmail());
    }
}
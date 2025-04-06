package com.scheduler.memberservice.member.admin.service;

import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.admin.domain.Admin;
import com.scheduler.memberservice.member.admin.repository.AdminJpaRepository;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.admin.WithAdmin;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindPasswordRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.PwdEditRequest;
import static com.scheduler.memberservice.testSet.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@IntegrationTest
class AdminCertServiceTest {

    @Autowired
    private AdminCertService adminCertService;

    @Autowired
    private AdminJpaRepository adminJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    @DisplayName("아이디와 이메일 회원가입 유무 확인 ")
    void emailConfirmation() {

        log.info("start emailConfirmation");
        FindPasswordRequest findPasswordRequest = new FindPasswordRequest();
        findPasswordRequest.setUsername(TEST_ADMIN_USERNAME);
        findPasswordRequest.setEmail(TEST_ADMIN_EMAIL);

        boolean b = adminCertService.emailConfirmation(findPasswordRequest);

        log.info("emailConfirmation result: {}", b);

        assertTrue(b);
    }

    @Test
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    @DisplayName("관리자 비밀번호 초기화")
    void initializePassword() {

        log.info("start initializePassword");

        //
        PwdEditRequest pwdEditRequest = new PwdEditRequest();
        pwdEditRequest.setPassword(TEST_ADMIN_PASSWORD);

        Admin admin = adminJpaRepository
                .findAdminByUsernameIs(TEST_ADMIN_USERNAME)
                .orElseThrow(MemberExistException::new);

        //
        adminCertService.initializePassword(pwdEditRequest);

        //
        boolean matches = passwordEncoder.matches(TEST_ADMIN_PASSWORD, admin.getPassword());

        assertTrue(matches);

    }

    @Test
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    @DisplayName("관리자 이메일 변경")
    void updateEmail() {

        EditEmailRequest editEmailRequest = new EditEmailRequest();
        editEmailRequest.setEmail(TEST_ADMIN_EMAIL);

        Admin admin = adminJpaRepository
                .findAdminByUsernameIs(TEST_ADMIN_USERNAME)
                .orElseThrow(MemberExistException::new);

        adminCertService.updateEmail(editEmailRequest);

        String email = admin.getEmail();

        assertEquals(TEST_ADMIN_EMAIL, email);
    }
}
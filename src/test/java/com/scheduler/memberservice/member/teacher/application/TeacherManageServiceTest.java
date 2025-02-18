package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.infra.IntegrationTest;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.scheduler.memberservice.infra.TestConstants.*;
import static com.scheduler.memberservice.infra.TestConstants.TEST_TEACHER_NAME;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.*;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class TeacherManageServiceTest {

    //== setting
    @Autowired
    private TeacherCertService teacherCertService;

    @Autowired
    private TeacherJpaRepository teacherJpaRepository;

    //== setting

    @Autowired
    private TeacherManageService teacherManageService;

    @BeforeEach
    void setUp() {
        TeacherInfoRequest.JoinTeacherRequest joinTeacherRequest = new TeacherInfoRequest.JoinTeacherRequest();
        joinTeacherRequest.setUsername(TEST_TEACHER_USERNAME);
        joinTeacherRequest.setPassword(TEST_TEACHER_PASSWORD);
        joinTeacherRequest.setEmail(TEST_TEACHER_EMAIL);
        joinTeacherRequest.setTeacherName(TEST_TEACHER_NAME);

        teacherCertService.joinTeacher(joinTeacherRequest);
    }

    @AfterEach
    void tearDown() {
        teacherJpaRepository.deleteAll();
    }

    @Test
    void getTeacherList() {
        List<TeacherResponse> teacherList =
                teacherManageService.getTeacherList();

        assertThat(teacherList.size()).isEqualTo(1);
    }

    @Test
    void findTeacherInformation() {

        TeacherResponse teacherInformation = teacherManageService
                .findTeacherInformation(TEST_TEACHER_USERNAME);

        assertThat(teacherInformation.getTeacherName())
                .isEqualTo(TEST_TEACHER_NAME);
    }

    @Test
    void changeTeacherStatus() {
        //
        Teacher before = teacherJpaRepository
                .findByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        //
        teacherManageService.changeTeacherStatus(TEST_TEACHER_USERNAME);

        //
        Teacher after = teacherJpaRepository
                .findByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        assertThat(before.getApproved()).isNotEqualTo(after.getApproved());
    }

    @Test
    void changeExistTeacher() {
    }
}
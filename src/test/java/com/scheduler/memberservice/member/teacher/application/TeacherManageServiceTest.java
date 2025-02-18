package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.infra.IntegrationTest;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.teacher.WithTeacher;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.scheduler.memberservice.infra.TestConstants.TEST_TEACHER_NAME;
import static com.scheduler.memberservice.infra.TestConstants.TEST_TEACHER_USERNAME;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;
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

    }

    @AfterEach
    void tearDown() {
        teacherJpaRepository.deleteAll();
    }

    @Test
    @WithTeacher(username = TEST_TEACHER_NAME)
    @DisplayName("등록된 교사 인원 수")
    void getTeacherList() {
        List<TeacherResponse> teacherList =
                teacherManageService.getTeacherList();

        assertThat(teacherList.size()).isEqualTo(1);
    }

    @Test
    @WithTeacher(username = TEST_TEACHER_NAME)
    @DisplayName("교사 정보 가져오기")
    void findTeacherInformation() {

        TeacherResponse teacherInformation = teacherManageService
                .findTeacherInformation(TEST_TEACHER_USERNAME);

        assertThat(teacherInformation.getTeacherName())
                .isEqualTo(TEST_TEACHER_NAME);
    }

//    @Test
    @WithTeacher(username = TEST_TEACHER_NAME)
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

//    @Test
    void changeExistTeacher() {
    }
}
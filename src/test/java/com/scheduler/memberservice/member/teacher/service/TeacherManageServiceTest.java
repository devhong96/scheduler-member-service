package com.scheduler.memberservice.member.teacher.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.util.MemberUtils;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseExistenceResponse;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;
import static com.scheduler.memberservice.testSet.TestConstants.TEST_TEACHER_NAME;
import static com.scheduler.memberservice.testSet.TestConstants.TEST_TEACHER_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@IntegrationTest
class TeacherManageServiceTest {

    //== setting
    @Autowired
    private TeacherJpaRepository teacherJpaRepository;

    @Autowired
    private TeacherManageService teacherManageService;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private MemberUtils memberUtils;

    @MockitoBean
    private CourseServiceClient courseServiceClient;
    //== setting

    @BeforeEach
    void startWireMockServer() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }
    }

    @AfterEach
    void stopWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    @DisplayName("교사 상태 변경")
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    void changeTeacherStatus()  {

        String teacherId = memberUtils.getTeacherForEntity().getTeacherId();

        //
        Teacher before = teacherJpaRepository
                .findTeacherByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(EntityNotFoundException::new);

        when(courseServiceClient.existWeeklyCoursesByTeacherId(teacherId))
                .thenReturn(new CourseExistenceResponse(false));

        Boolean beforeApproved = before.getApproved();

        teacherManageService.changeTeacherStatus(TEST_TEACHER_USERNAME);

        //
        Teacher after = teacherJpaRepository
                .findTeacherByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        Boolean afterApproved = after.getApproved();

        assertThat(beforeApproved)
                .isNotEqualTo(afterApproved);
    }

    @Test
    @DisplayName("등록된 교사 인원 수")
    void getTeacherList() {
        List<TeacherResponse> teacherList =
                teacherManageService.getTeacherList();

        assertThat(teacherList.size()).isEqualTo(3);
    }

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("교사 정보 가져오기")
    void findTeacherInformation() {

        TeacherResponse teacherInformation = teacherManageService
                .findTeacherInformation(TEST_TEACHER_USERNAME);

        assertThat(teacherInformation.getTeacherName())
                .isEqualTo(TEST_TEACHER_NAME);
    }
}
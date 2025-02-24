package com.scheduler.memberservice.member.teacher.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.util.MemberUtils;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseExistenceResponse;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;
import static com.scheduler.memberservice.testSet.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@IntegrationTest
class TeacherManageServiceTest {

    //== setting
    @Autowired
    private TeacherJpaRepository teacherJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeacherManageService teacherManageService;

    @MockitoBean
    private WireMockServer wireMockServer;

    @Autowired
    private MemberUtils memberUtils;
    //== setting

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(
                wireMockConfig()
                .port(8080));
        wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
        teacherJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("교사 상태 변경")
    @WithTeacher(username = TEST_TEACHER_NAME)
    void changeTeacherStatus() throws JsonProcessingException {

        String teacherId = memberUtils.getTeacherId();

        final String expectedResponse = objectMapper
                .writeValueAsString(new CourseExistenceResponse(false)
        );

        //
        Teacher before = teacherJpaRepository
                .findTeacherByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        Boolean beforeApproved = before.getApproved();

        stubFor(get(urlEqualTo(
                BASE_URL + "/teacher/" + teacherId + "/courses"))
//                .withHeader("Authorization", matching(".*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(expectedResponse))
        );

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
}
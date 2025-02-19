package com.scheduler.memberservice.member.teacher.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;
import static com.scheduler.memberservice.testSet.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class TeacherManageServiceTest {

    //== setting
    @Autowired
    private TeacherCertService teacherCertService;

    @Autowired
    private TeacherJpaRepository teacherJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    void changeTeacherStatus() throws JsonProcessingException {

        final String loginId = "sh111";
        final String expectedResponse = objectMapper.writeValueAsString(
                Map.of(
                        "memberId", 1,
                        "nickname", "성하"
                )
        );

        //
        Teacher before = teacherJpaRepository
                .findByUsernameIs(TEST_TEACHER_USERNAME)
                .orElseThrow(MemberExistException::new);

        //
        stubFor(get(urlEqualTo(BASE_URL ))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(expectedResponse))
        );
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
package com.scheduler.memberservice.member.student.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static com.scheduler.memberservice.testSet.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@IntegrationTest
class StudentManageServiceTest {

    @MockitoBean
    private CourseServiceClient courseServiceClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentCertService studentCertService;

    @Autowired
    private StudentManageService studentManageService;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("학생 조회")
    void findStudentInfoList() {

        Page<StudentInfoResponse> studentInfoList =
                studentManageService.findStudentInfoList(
                        TEST_TEACHER_NAME,
                        TEST_STUDENT_NAME,
                        of(0, 10)
                );

        assertThat(studentInfoList).size().isEqualTo(0);
    }

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("학생 상태 변경")
    void changeStudentStatus() {

        Student student = studentJpaRepository
                .findStudentByUsernameIs(TEST_STUDENT_USERNAME)
                .orElseThrow(MemberExistException::new);

        Boolean approved = student.getApproved();

        studentManageService.changeStudentStatus(student.getStudentId());

        Student resultStudent = studentJpaRepository.findStudentByUsernameIs(student.getUsername()).orElseThrow();

        Boolean resultApproved = resultStudent.getApproved();

        assertThat(resultApproved).isNotEqualTo(approved);
    }

//    @Test
    @DisplayName("교사 변경")
    @WithTeacher(username = TEST_TEACHER_NAME)
    void changeExistTeacher() throws JsonProcessingException {

        final String expectedResponse = objectMapper
                .writeValueAsString(
                        Map.of(
                                "result", true
                        )
                );

        stubFor(get(urlEqualTo(
                BASE_URL + "/" + "teacher" + "/" + TEST_TEACHER_ID + "/" + "student" + "/" + TEST_STUDENT_ID))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(expectedResponse))
        );

        ChangeTeacherRequest changeTeacherRequest = new ChangeTeacherRequest();
        changeTeacherRequest.setTeacherId(TEST_TEACHER_ID);
        changeTeacherRequest.setStudentId(TEST_STUDENT_ID);
        studentManageService.changeExistTeacher(changeTeacherRequest);

        Student student = studentJpaRepository.findStudentByUsernameIs(TEST_STUDENT_USERNAME)
                .orElseThrow(MemberExistException::new);

        String teacherId = student.getTeacherId();
        assertThat(TEST_TEACHER_ID).isEqualTo(teacherId);
    }

    @BeforeEach
    void setUp() {

        RegisterStudentRequest request = new RegisterStudentRequest();

        request.setUsername(TEST_STUDENT_USERNAME);
        request.setStudentName(TEST_STUDENT_NAME);
        request.setPassword(TEST_STUDENT_PASSWORD);
        request.setStudentEmail(TEST_STUDENT_EMAIL);
        request.setStudentPhoneNumber(TEST_STUDENT_PHONE_NUMBER);
        request.setStudentAddress(TEST_STUDENT_ADDRESS);
        request.setStudentDetailedAddress(TEST_STUDENT_DETAILED_ADDRESS);
        request.setStudentParentPhoneNumber(TEST_STUDENT_PARENT_PHONE_NUMBER);
        studentCertService.registerStudent(request);
    }

    @AfterEach
    void tearDown() {
        studentJpaRepository.deleteAll();
    }

}
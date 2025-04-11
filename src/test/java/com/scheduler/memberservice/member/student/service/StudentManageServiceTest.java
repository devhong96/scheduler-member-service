package com.scheduler.memberservice.member.student.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.dto.StudentRequest;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.messaging.TestRabbitConsumer;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseReassignmentResponse;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.*;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static com.scheduler.memberservice.testSet.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.PageRequest.of;

@IntegrationTest
@Import(TestRabbitConsumer.class)
class StudentManageServiceTest {

    @Autowired
    private StudentCertService studentCertService;

    @Autowired
    private StudentManageService studentManageService;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private TestRabbitConsumer testRabbitConsumer;

    @Autowired
    private WireMockServer wireMockServer;

    @MockitoBean
    private CourseServiceClient courseServiceClient;

    @AfterEach
    void stopWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void setUp() {

        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }

        RegisterStudentRequest request = new RegisterStudentRequest();

        request.setStudentId(TEST_STUDENT_ID);
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

    @Test
    @DisplayName("레빗엠큐 테스트")
    void changeStudentName() throws InterruptedException {

        // Given
        StudentRequest.ChangeStudentNameRequest changeStudentNameRequest = new StudentRequest.ChangeStudentNameRequest();
        changeStudentNameRequest.setStudentId(TEST_STUDENT_ID);
        changeStudentNameRequest.setStudentName(TEST_STUDENT_NAME);

        // When
        studentManageService.changeStudentName(changeStudentNameRequest);

        // Then
        ChangeStudentNameRequest received = testRabbitConsumer.getReceivedMessage();

        assertThat(received)
                .extracting("newName", "memberId")
                .containsExactly(TEST_STUDENT_NAME, TEST_STUDENT_ID);
    }

    @Test
    @DisplayName("교사 변경")
    @WithTeacher(username = TEST_TEACHER_NAME, teacherId = TEST_TEACHER_ID)
    void changeExistTeacher() {

        when(courseServiceClient.validateStudentCoursesAndReassign(TEST_TEACHER_ID, TEST_STUDENT_ID))
                .thenReturn(new CourseReassignmentResponse(true));

        ChangeTeacherRequest changeTeacherRequest = new ChangeTeacherRequest();
        changeTeacherRequest.setTeacherId(TEST_TEACHER_ID);
        changeTeacherRequest.setStudentId(TEST_STUDENT_ID);

        studentManageService.changeExistTeacher(changeTeacherRequest);

        Student student = studentJpaRepository
                .findStudentByUsernameIs(TEST_STUDENT_USERNAME)
                .orElseThrow(MemberExistException::new);

        String teacherId = student.getTeacherId();

        assertThat(TEST_TEACHER_ID).isEqualTo(teacherId);
    }
}
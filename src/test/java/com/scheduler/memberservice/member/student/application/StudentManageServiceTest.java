package com.scheduler.memberservice.member.student.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.messaging.TestRabbitConsumer;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import org.junit.jupiter.api.*;
import org.mockito.Spy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.testcontainers.containers.RabbitMQContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseReassignmentResponse;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.*;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static com.scheduler.memberservice.testSet.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@IntegrationTest
@Import(TestRabbitConsumer.class)
class StudentManageServiceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentCertService studentCertService;

    @Autowired
    private StudentManageService studentManageService;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private TestRabbitConsumer testRabbitConsumer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Spy
    private static WireMockServer wireMockServer;

    private static final RabbitMQContainer RABBITMQ_CONTAINER =
            new RabbitMQContainer("rabbitmq:3-management");

    static {
        RABBITMQ_CONTAINER.start();
        System.setProperty("spring.rabbitmq.host", RABBITMQ_CONTAINER.getHost());
        System.setProperty("spring.rabbitmq.port", RABBITMQ_CONTAINER.getAmqpPort().toString());
    }

    @BeforeAll
    static void startWireMockServer() {
        wireMockServer = new WireMockServer(wireMockConfig().port(8080));
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void setUp() {

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

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
        studentJpaRepository.deleteAll();
        studentJpaRepository.flush();
        rabbitTemplate.stop();
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
        ChangeStudentName changeStudentName = new ChangeStudentName();
        changeStudentName.setStudentId(TEST_STUDENT_ID);
        changeStudentName.setStudentName(TEST_STUDENT_NAME);

        // When
        studentManageService.changeStudentName(changeStudentName);

        // Then
        ChangeStudentNameRequest received = testRabbitConsumer.getReceivedMessage();

        assertThat(received)
                .extracting("studentName", "studentId")
                .containsExactly(TEST_STUDENT_NAME, TEST_STUDENT_ID);
    }

    @Test
    @DisplayName("교사 변경")
    @WithTeacher(username = TEST_TEACHER_NAME, teacherId = TEST_TEACHER_ID)
    void changeExistTeacher() throws JsonProcessingException {

        stubFor(patch(urlEqualTo(
                "/feign-course/teacher/" + TEST_TEACHER_ID + "/student/" + TEST_STUDENT_ID))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(objectMapper
                                .writeValueAsString(
                                        new CourseReassignmentResponse(true)
                                )))
        );

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
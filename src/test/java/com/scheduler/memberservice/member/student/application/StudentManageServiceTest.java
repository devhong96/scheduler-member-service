package com.scheduler.memberservice.member.student.application;

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

import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static com.scheduler.memberservice.testSet.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.PageRequest.of;

@IntegrationTest
class StudentManageServiceTest {

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

    @BeforeEach
    void setUp() {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setUsername(TEST_STUDENT_USERNAME);
        request.setStudentName(TEST_STUDENT_NAME);
        request.setPassword(TEST_STUDENT_PASSWORD);
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
package com.scheduler.memberservice.member.student.application;

import com.scheduler.memberservice.infra.IntegrationTest;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.teacher.WithTeacher;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Optional;

import static com.scheduler.memberservice.infra.TestConstants.*;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.domain.PageRequest.of;

@IntegrationTest
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("학생 등록")
    void registerStudentInformation() {

        Optional<Student> studentByStudentName = studentJpaRepository
                .findStudentByStudentName(TEST_STUDENT_NAME);

        assertTrue(studentByStudentName.isPresent(), "조회된 학생이 존재해야 합니다.");

        assertThat(studentByStudentName)
                .isPresent()
                .get()
                .extracting(Student::getStudentName, Student::getStudentPhoneNumber)
                .containsExactly(TEST_STUDENT_NAME, TEST_STUDENT_PHONE_NUMBER);
    }

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("학생 등록 변경")
    void modifyStudentVerification() {

        //
        Student student = studentJpaRepository
                .findStudentByStudentName(TEST_STUDENT_NAME)
                .orElseThrow(MemberExistException::new);

        student.updateApproved(false);
        Student result = studentJpaRepository.save(student);

        //
        assertFalse(result.getApproved());

    }

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("학생 조회")
    void findStudentInfoList() {

        Page<StudentInfoResponse> studentInfoList = studentService
                .findStudentInfoList(TEST_TEACHER_NAME, TEST_STUDENT_NAME, of(0, 10));

        assertThat(studentInfoList).size().isEqualTo(1);
    }

    @BeforeEach
    void setUp() {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setStudentName(TEST_STUDENT_NAME);
        request.setPassword(TEST_STUDENT_PASSWORD);
        request.setStudentPhoneNumber(TEST_STUDENT_PHONE_NUMBER);
        request.setStudentAddress(TEST_STUDENT_ADDRESS);
        request.setStudentDetailedAddress(TEST_STUDENT_DETAILED_ADDRESS);
        request.setStudentParentPhoneNumber(TEST_STUDENT_PARENT_PHONE_NUMBER);
        studentService.registerStudentInformation(request);
    }

    @AfterEach
    void tearDown() {
        studentJpaRepository.deleteAll();
    }
}
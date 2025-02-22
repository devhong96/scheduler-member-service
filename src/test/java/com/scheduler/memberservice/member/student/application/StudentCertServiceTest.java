package com.scheduler.memberservice.member.student.application;

import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.student.WithStudent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.ModifyStudentInfoRequest;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.ModifyStudentPasswordRequest;
import static com.scheduler.memberservice.testSet.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class StudentCertServiceTest {

    @Autowired
    private StudentCertService studentCertService;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithStudent(username = TEST_STUDENT_USERNAME)
    @DisplayName("학생 등록")
    void registerStudent() {

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
    @WithStudent(username = TEST_STUDENT_USERNAME)
    @DisplayName("학생 개인정보 변경")
    void modifyStudentInfo() {

        ModifyStudentInfoRequest modifyStudentInfoRequest = new ModifyStudentInfoRequest();

        modifyStudentInfoRequest.setStudentPhoneNumber(TEST_STUDENT_PHONE_NUMBER);
        modifyStudentInfoRequest.setStudentEmail("student@email.com");
        modifyStudentInfoRequest.setStudentAddress(TEST_STUDENT_ADDRESS);
        modifyStudentInfoRequest.setStudentDetailedAddress(TEST_STUDENT_DETAILED_ADDRESS);
        modifyStudentInfoRequest.setStudentParentPhoneNumber(TEST_STUDENT_PARENT_PHONE_NUMBER);

        studentCertService.modifyStudentInfo(modifyStudentInfoRequest);



        Student student = studentJpaRepository
                .findStudentByUsernameIs(TEST_STUDENT_NAME)
                .orElseThrow(MemberExistException::new);
        
        student.modifyStudentInfo(modifyStudentInfoRequest);

        Student resultStudent = studentJpaRepository
                .findStudentByUsernameIs(TEST_STUDENT_NAME)
                .orElseThrow(MemberExistException::new);

        String email = resultStudent.getEmail();

        assertThat(email).isEqualTo("student@email.com");

    }

    @Test
    @WithStudent(username = TEST_STUDENT_USERNAME)
    @DisplayName("학생 비밀번호 변경")
    void modifyStudentPassword() {

        ModifyStudentPasswordRequest modifyStudentPasswordRequest = new ModifyStudentPasswordRequest();
        modifyStudentPasswordRequest.setNewPassword("newPassword");
        modifyStudentPasswordRequest.setConfirmNewPassword("newPassword");

        studentCertService.modifyStudentPassword(modifyStudentPasswordRequest);

        Student student = studentJpaRepository
                .findStudentByUsernameIs(TEST_STUDENT_NAME)
                .orElseThrow(MemberExistException::new);

        boolean matches = passwordEncoder.matches(modifyStudentPasswordRequest.getNewPassword(), student.getPassword());

        assertTrue(matches);
    }

    @AfterEach
    void tearDown() {
        studentJpaRepository.deleteAll();
    }
}
package com.scheduler.memberservice.member.student.application;

import com.scheduler.memberservice.infra.util.MemberUtils;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.*;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final MemberUtils memberUtils;
    private final StudentJpaRepository studentJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerStudent(
            RegisterStudentRequest registerStudentRequest
    ) {
        Student student = Student.create(registerStudentRequest, "default", passwordEncoder);
        studentJpaRepository.save(student);
    }

    @Override
    @Transactional
    public void modifyStudentInfo(
            ModifyStudentInfoRequest modifyStudentRequest
    ) {
        Student student = memberUtils.getStudent();
        student.modifyStudentInfo(modifyStudentRequest);
    }

    @Override
    @Transactional
    public void modifyStudentPassword(
            ModifyStudentPasswordRequest modifyStudentPasswordRequest
    ) {
        String newPassword = modifyStudentPasswordRequest.getNewPassword();
        String confirmNewPassword = modifyStudentPasswordRequest.getConfirmNewPassword();

        if(!Objects.equals(newPassword, confirmNewPassword)){
            throw new RuntimeException("Passwords do not match");
        }

        Student student = memberUtils.getStudent();
        student.changePassword(modifyStudentPasswordRequest, passwordEncoder);
    }
}

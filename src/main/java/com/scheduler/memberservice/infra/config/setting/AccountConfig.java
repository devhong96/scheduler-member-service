package com.scheduler.memberservice.infra.config.setting;

import com.scheduler.memberservice.member.admin.domain.Admin;
import com.scheduler.memberservice.member.admin.repository.AdminJpaRepository;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.JoinTeacherRequest;

@Component
@RequiredArgsConstructor
public class AccountConfig implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final AdminJpaRepository adminJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;
    private final StudentJpaRepository studentJpaRepository;

    @Override
    public void run(ApplicationArguments args) {

        final String admin = "admin";
        final String password = "password";
        final String email = "email";
        Teacher teacherEntity = null;

        if (!adminJpaRepository.existsAdminByUsername(admin)) {
            adminJpaRepository.save(Admin.create(admin, password, email, passwordEncoder));
        }

        final String teacher = "teacher";

        JoinTeacherRequest joinTeacherRequest = new JoinTeacherRequest();
        joinTeacherRequest.setUsername(teacher);
        joinTeacherRequest.setPassword(password);
        joinTeacherRequest.setTeacherName(teacher);
        joinTeacherRequest.setEmail(email);

        if (!teacherJpaRepository.existsTeacherByUsername(teacher)) {
            teacherEntity = teacherJpaRepository.save(Teacher.create(joinTeacherRequest, passwordEncoder));
        }

        final String student = "student";

        RegisterStudentRequest studentRequest = new RegisterStudentRequest();

        studentRequest.setUsername(student);
        studentRequest.setPassword(password);
        studentRequest.setStudentName("studentName");
        studentRequest.setStudentEmail(email);
        studentRequest.setStudentAddress("studentAddress");
        studentRequest.setStudentDetailedAddress("studentDetailedAddress");
        studentRequest.setStudentPhoneNumber("studentPhoneNumber");
        studentRequest.setStudentParentPhoneNumber("studentParentPhoneNumber");

        if (!studentJpaRepository.existsStudentByUsername(student)) {
            studentJpaRepository.save(Student.create(studentRequest, teacherEntity.getTeacherId(), passwordEncoder));
        }

    }
}

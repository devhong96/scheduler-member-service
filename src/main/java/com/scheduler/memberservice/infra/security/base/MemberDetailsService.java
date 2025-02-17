package com.scheduler.memberservice.infra.security.base;

import com.scheduler.memberservice.infra.exception.custom.AuthorApproveException;
import com.scheduler.memberservice.member.admin.domain.Admin;
import com.scheduler.memberservice.member.admin.repository.AdminJpaRepository;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static com.scheduler.memberservice.infra.log.IPLog.getIpAddress;


@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final AdminJpaRepository adminJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;
    private final StudentJpaRepository studentJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("user = {}, time = {}, IpAddress = {}", username, LocalDate.now(), getIpAddress());

        Optional<Student> studentByUsernameIs = studentJpaRepository.findStudentByUsernameIs(username);

        if (studentByUsernameIs.isPresent()) {

            Student student = studentByUsernameIs.get();
            StudentDetails studentDetails = new StudentDetails(student);

            if (studentDetails.isEnabled()){
                return new StudentDetails(student);
            }

            throw new AuthorApproveException();
        }

        Optional<Teacher> teacherByUsername = teacherJpaRepository.findByUsernameIs(username);

        if (teacherByUsername.isPresent()) {

            Teacher teacher = teacherByUsername.get();
            TeacherDetails authorDetails = new TeacherDetails(teacher);

            if (authorDetails.isEnabled()){
                return new TeacherDetails(teacher);
            }

            throw new AuthorApproveException();
        }

        Optional<Admin> adminByUsername = adminJpaRepository.findByUsernameIs(username);

        if (adminByUsername.isPresent()) {
            Admin admin = adminByUsername.get();
            return new AdminDetails(admin);
        }

        throw new UsernameNotFoundException(username);
    }
}

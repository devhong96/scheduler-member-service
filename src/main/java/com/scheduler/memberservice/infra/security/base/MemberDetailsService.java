package com.scheduler.memberservice.infra.security.base;

import com.scheduler.memberservice.infra.exception.custom.AuthorApproveException;
import com.scheduler.memberservice.member.admin.repository.AdminJpaRepository;
import com.scheduler.memberservice.member.student.application.StudentService;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.teacher.application.TeacherService;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.scheduler.memberservice.infra.log.IPLog.getIpAddress;


@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final AdminJpaRepository adminJpaRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("user = {}, time = {}, IpAddress = {}", username, LocalDate.now(), getIpAddress());

        try {
            Student student = studentService.findStudentByUsernameIs(username);
            StudentDetails studentDetails = new StudentDetails(student);
            if (studentDetails.isEnabled()) {
                return studentDetails;
            }
            throw new AuthorApproveException();
        } catch (EntityNotFoundException e) {
            log.debug("학생 아님: {}", username);
        }

        try {
            Teacher teacher = teacherService.findTeacherByUsernameIs(username);
            TeacherDetails teacherDetails = new TeacherDetails(teacher);
            if (teacherDetails.isEnabled()) {
                return teacherDetails;
            }
            throw new AuthorApproveException();
        } catch (EntityNotFoundException e) {
            log.debug("교사 아님: {}", username);
        }

        return adminJpaRepository.findAdminByUsernameIs(username)
                .map(AdminDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}

package com.scheduler.memberservice.client.service;

import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.member.common.RoleType;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.*;
import static com.scheduler.memberservice.member.common.RoleType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeignCourseServiceImpl implements FeignCourseService {

    private final JwtUtils jwtUtils;
    private final TeacherJpaRepository teacherJpaRepository;
    private final StudentJpaRepository studentJpaRepository;

    @Override
    public TeacherInfo findTeacherInfoByToken(String token) {
        token = token.replace("Bearer ", "").trim();

        String username = jwtUtils.getAuthentication(token).getName();
        Teacher teacher = teacherJpaRepository
                .findTeacherByUsernameIs(username)
                .orElseThrow(MemberExistException::new);

        return new TeacherInfo(teacher.getTeacherId());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentInfo findStudentInfoByToken(String token) {

        token = token.replace("Bearer ", "").trim();

        Authentication authentication = jwtUtils.getAuthentication(token);

        String username = authentication.getName();

        Student student = studentJpaRepository
                .findStudentByUsernameIs(username)
                .orElseThrow(MemberExistException::new);

        log.info(student.toString());

        String teacherId = student.getTeacherId();

        log.info(teacherId);

        Teacher teacher = teacherJpaRepository
                .findTeacherByTeacherId(teacherId)
                .orElseThrow(MemberExistException::new);

        String teacherName = teacher.getTeacherName();
        String studentId = student.getStudentId();
        String studentName = student.getStudentName();

        log.info(studentName);

        return new StudentInfo(studentId, studentName, teacherId, teacherName);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo findMemberInfoByToken(String token) {

        Authentication authentication = jwtUtils.getAuthentication(token);

        RoleType roleType = valueOf(authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList().get(0));

        switch (roleType) {

            case TEACHER: {
                Teacher teacher = teacherJpaRepository
                        .findTeacherByUsernameIs(authentication.getName())
                        .orElseThrow(MemberExistException::new);

                String teacherId = teacher.getTeacherId();

                return new MemberInfo(TEACHER, teacherId);
            }

            case STUDENT: {
                Student student = studentJpaRepository
                        .findStudentByUsernameIs(authentication.getName())
                        .orElseThrow(MemberExistException::new);

                String studentId = student.getStudentId();

                return new MemberInfo(STUDENT, studentId);
            }
        }

        throw new MemberExistException();
    }
}

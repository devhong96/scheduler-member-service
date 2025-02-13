package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.infra.exception.custom.DuplicateEmailException;
import com.scheduler.memberservice.infra.exception.custom.DuplicateUsernameException;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.JoinTeacherRequest;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherJpaRepository teacherJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void joinTeacher(JoinTeacherRequest joinTeacherRequest) {

        boolean existsByUsername = teacherJpaRepository.existsByUsername(joinTeacherRequest.getUsername());

        if (existsByUsername) {
            throw new DuplicateUsernameException();
        }

        boolean existsByEmail = teacherJpaRepository.existsByEmail(joinTeacherRequest.getEmail());

        if (existsByEmail) {
            throw new DuplicateEmailException();
        }

        Teacher teacher = Teacher.create(joinTeacherRequest, passwordEncoder);
        teacherJpaRepository.save(teacher);
    }
}

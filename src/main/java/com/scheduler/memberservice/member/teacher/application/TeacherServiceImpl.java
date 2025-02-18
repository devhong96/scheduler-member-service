package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherJpaRepository teacherJpaRepository;
    private final PasswordEncoder passwordEncoder;


}

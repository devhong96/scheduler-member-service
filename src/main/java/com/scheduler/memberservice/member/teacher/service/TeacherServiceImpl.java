package com.scheduler.memberservice.member.teacher.service;

import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherJpaRepository teacherJpaRepository;

    @Cacheable(
        cacheNames = "teachersByUsername",
        key = "'teacher:username:' + #username",
        cacheManager = "authCacheManager"
    )
    public Teacher findTeacherByUsernameIs(String username) {
        return teacherJpaRepository.findTeacherByUsernameIs(username)
                .orElseThrow(EntityNotFoundException::new);
    }
}

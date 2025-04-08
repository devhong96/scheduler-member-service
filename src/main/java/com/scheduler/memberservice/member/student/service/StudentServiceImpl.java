package com.scheduler.memberservice.member.student.service;

import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentJpaRepository studentJpaRepository;

    @Cacheable(
            cacheNames = "studentsByUsername",
            key = "'student:username:' + #username",
            cacheManager = "authCacheManager"
    )
    public Student findStudentByUsernameIs(String username) {
        return studentJpaRepository.findStudentByUsernameIs(username)
                .orElseThrow(() -> new EntityNotFoundException("학생을 찾을 수 없습니다: " + username));
    }
}

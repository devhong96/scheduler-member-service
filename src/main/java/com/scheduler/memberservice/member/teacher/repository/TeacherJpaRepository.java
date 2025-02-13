package com.scheduler.memberservice.member.teacher.repository;

import com.scheduler.memberservice.member.teacher.domain.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherJpaRepository extends JpaRepository<Teacher, Long> {

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<Teacher> findByUsernameIs(String username);

    Optional<Teacher> findTeacherByTeacherId(String teacherId);

    Optional<Teacher> findByEmailIs(String email);

    Boolean existsTeacherByUsernameAndEmail(String username, String email);

    @Transactional
    void deleteByUsernameIs(String username);

    Optional<Teacher> findByTeacherId(String teacherId);
}

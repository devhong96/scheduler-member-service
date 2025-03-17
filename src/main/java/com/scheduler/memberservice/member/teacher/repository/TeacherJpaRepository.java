package com.scheduler.memberservice.member.teacher.repository;

import com.scheduler.memberservice.member.teacher.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherJpaRepository extends JpaRepository<Teacher, Long> {

    Boolean existsTeacherByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<Teacher> findTeacherByUsernameIs(String username);

    Optional<Teacher> findTeacherByTeacherId(String teacherId);

    Optional<Teacher> findByEmailIs(String email);

    Boolean existsTeacherByUsernameAndEmail(String username, String email);

}

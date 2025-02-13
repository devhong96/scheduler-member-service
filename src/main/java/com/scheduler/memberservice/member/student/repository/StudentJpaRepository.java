package com.scheduler.memberservice.member.student.repository;

import com.scheduler.memberservice.member.student.domain.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentJpaRepository extends JpaRepository<Student, Long> {

    boolean existsByStudentNameIs(String StudentName);

    Optional<Student> findStudentByStudentId(String studentId);

    Student findStudentEntityByStudentName(String studentName);

    @Transactional
    void deleteStudentEntityById(long id);
}
package com.scheduler.memberservice.member.student.repository;

import com.scheduler.memberservice.member.student.domain.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentJpaRepository extends JpaRepository<Student, Long> {

    Optional<Student> findStudentByStudentId(String studentId);

    Optional<Student> findStudentByStudentName(String studentName);

    @Transactional
    void deleteStudentEntityById(long id);
}
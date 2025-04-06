package com.scheduler.memberservice.member.student.service;

import com.scheduler.memberservice.member.student.domain.Student;

public interface StudentService {

    Student findStudentByUsernameIs(String username);
}

package com.scheduler.memberservice.member.student.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeStudentNameRequest;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;

public interface StudentManageService {

    Page<StudentInfoResponse> findStudentInfoList(String teacherName, String studentName, Pageable pageable);

    void changeStudentStatus(String studentId);

    void changeExistTeacher(ChangeTeacherRequest changeTeacherRequest);

    void changeStudentName(ChangeStudentNameRequest changeStudentNameRequest);
}

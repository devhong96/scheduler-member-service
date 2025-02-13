package com.scheduler.memberservice.member.student.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.ModifyStudentRequest;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;

public interface StudentService {

    void registerStudentInformation(RegisterStudentRequest registerStudentRequest);

    void modifyStudentVerification(ModifyStudentRequest registerStudentRequest);

    Page<StudentInfoResponse> findStudentInfoList(String teacherName, String studentName, Pageable pageable);
}

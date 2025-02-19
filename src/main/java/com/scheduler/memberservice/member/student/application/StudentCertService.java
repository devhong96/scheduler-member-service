package com.scheduler.memberservice.member.student.application;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.*;

public interface StudentCertService {

    void registerStudent(RegisterStudentRequest registerStudentRequest);

    void modifyStudentInfo(ModifyStudentInfoRequest modifyStudentRequest);

    void modifyStudentPassword(ModifyStudentPasswordRequest modifyStudentPasswordRequest);
}

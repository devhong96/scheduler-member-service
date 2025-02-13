package com.scheduler.memberservice.member.admin.application;

import java.util.List;

import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.EmailRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;

public interface AdminService {

    List<TeacherResponse> getTeacherList();

    List<TeacherResponse> findTeacherInformation(String username);

    void findAdminUsernameByEmail(EmailRequest emailRequest);

    void grantAuth(String teacherId);
    void revokeAuth(String teacherId);

    void changeExistTeacher(ChangeTeacherRequest changeTeacherRequest);
    void deleteTeacherAccount(String teacherId);

    String findEmail();
}

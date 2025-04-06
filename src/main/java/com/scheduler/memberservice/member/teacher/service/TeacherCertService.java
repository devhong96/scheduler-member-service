package com.scheduler.memberservice.member.teacher.service;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindPasswordRequest;
import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindUsernameRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.*;

public interface TeacherCertService {

    void joinTeacher(JoinTeacherRequest joinTeacherRequest);

    void findUsernameByEmail(FindUsernameRequest findUsernameRequest);

    void sendPasswordResetEmail(FindPasswordRequest findPasswordRequest);

    void initializePassword(PwdEditRequest pwdEditRequest);

    void changeTeacherEmail(EditEmailRequest editEmailRequest);
}

package com.scheduler.memberservice.member.teacher.application;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindPasswordRequest;
import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindUsernameRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.PwdEditRequest;

public interface TeacherCertService {

    void findUsernameByEmail(FindUsernameRequest findUsernameRequest);

    void sendPasswordResetEmail(FindPasswordRequest findPasswordRequest);

    void initializePassword(PwdEditRequest pwdEditRequest);

    void changeUserEmail(EditEmailRequest editEmailRequest);
}

package com.scheduler.memberservice.member.teacher.application;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.*;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.PwdEditRequest;

public interface TeacherCertService {

    void findUsernameByEmail(FindIdRequest findIdRequest);

    void initializePassword(PwdEditRequest pwdEditRequest);

    void sendPasswordResetEmail(FindPasswordRequest findPasswordRequest);

    void verifyAuthCode(AuthCodeRequest authCodeRequest);

    void changeUserEmail(EditEmailRequest editEmailRequest);
}

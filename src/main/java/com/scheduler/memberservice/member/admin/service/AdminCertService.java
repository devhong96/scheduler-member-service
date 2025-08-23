package com.scheduler.memberservice.member.admin.service;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindPasswordRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.PwdEditRequest;

public interface AdminCertService {

    boolean emailConfirmation(FindPasswordRequest findPasswordRequest);

    void initializePassword(PwdEditRequest pwdEditRequest);

    void updateEmail(EditEmailRequest editEmailRequest);
}

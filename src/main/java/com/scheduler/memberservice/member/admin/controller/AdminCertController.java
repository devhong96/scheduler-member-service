package com.scheduler.memberservice.member.admin.controller;

import com.scheduler.memberservice.member.admin.application.AdminCertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.PwdEditRequest;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/help")
public class AdminCertController {

    public final AdminCertService adminCertService;

    @PatchMapping("password")
    public ResponseEntity<String> initializePassword(
            @Valid @RequestBody PwdEditRequest pwdEditRequest
    ) {
        adminCertService.initializePassword(pwdEditRequest);
        return new ResponseEntity<>(OK);
    }

    @PatchMapping("email")
    public ResponseEntity<String> updateEmail(
            @Valid EditEmailRequest editEmailRequest
    ) {
        adminCertService.updateEmail(editEmailRequest);
        return new ResponseEntity<>(OK);
    }
}

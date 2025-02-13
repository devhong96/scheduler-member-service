package com.scheduler.memberservice.member.admin.controller;

import com.scheduler.memberservice.member.admin.application.AdminCertService;
import com.scheduler.memberservice.member.admin.application.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.PwdEditRequest;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/help")
public class AdminCertController {

    public final AdminService adminService;
    public final AdminCertService adminCertService;

    @PatchMapping("password")
    public ResponseEntity<String> initializePassword(@RequestBody PwdEditRequest pwdEditRequest) {
        adminCertService.initializePassword(pwdEditRequest);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("email")
    public ResponseEntity<String> getEmail(){
        return new ResponseEntity<>(adminService.findEmail(), OK);
    }

    @PatchMapping("email")
    public ResponseEntity<String> updateEmail(EditEmailRequest editEmailRequest) {
        adminCertService.updateEmail(editEmailRequest);
        return new ResponseEntity<>(OK);
    }
}

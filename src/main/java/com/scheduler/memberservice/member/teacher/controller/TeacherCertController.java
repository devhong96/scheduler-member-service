package com.scheduler.memberservice.member.teacher.controller;

import com.scheduler.memberservice.infra.email.dto.AuthEmailService;
import com.scheduler.memberservice.member.teacher.application.TeacherCertService;
import com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.*;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.EditEmailRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.PwdEditRequest;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("help")
@RequiredArgsConstructor
public class TeacherCertController {

    private final TeacherCertService teacherCertService;
    private final AuthEmailService authEmailService;

    @Operation(description = "교사 회원 가입")
    @PostMapping
    public ResponseEntity<Void> approved(
            @Valid @RequestBody TeacherInfoRequest.JoinTeacherRequest joinTeacherRequest
    ) {
        teacherCertService.joinTeacher(joinTeacherRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(description = "아이디 찾기")
    @PostMapping("sendUsername")
    public ResponseEntity<String> findUsernameByEmail(
            @Valid @RequestBody FindUsernameRequest findUsernameRequest
    ) {
        teacherCertService.findUsernameByEmail(findUsernameRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(description = "이메일이 있으면 메일 보냄")
    @PostMapping("findPwd")
    public ResponseEntity<String> sendPasswordResetEmail(
            @Valid @RequestBody FindPasswordRequest findPasswordRequest
    ) {
        teacherCertService.sendPasswordResetEmail(findPasswordRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(description = "인증번호 인증")
    @PostMapping("authNumCheck")
    public ResponseEntity<String> verifyAuthCode(
            @Valid @RequestBody AuthCodeRequest authCodeRequest
    ) {
        authEmailService.verifyAuthCode(authCodeRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(description = "확인 후 변경")
    @PatchMapping("password")
    public ResponseEntity<String> initializePassword(
            @Valid @RequestBody PwdEditRequest pwdEditRequest
    ) {
        teacherCertService.initializePassword(pwdEditRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(description = "로그인 상태에서 이메일 변경")
    @PatchMapping("email")
    public ResponseEntity<Void> changeUserEmail(
            @Valid @RequestBody EditEmailRequest editEmailRequest
    ) {
        teacherCertService.changeUserEmail(editEmailRequest);
        return new ResponseEntity<>(OK);
    }
}

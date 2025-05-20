package com.scheduler.memberservice.member.teacher.controller;

import com.scheduler.memberservice.infra.email.application.AuthEmailService;
import com.scheduler.memberservice.member.teacher.service.TeacherCertService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.*;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("teacher")
@RequiredArgsConstructor
public class TeacherCertController {

    private final TeacherCertService teacherCertService;
    private final AuthEmailService authEmailService;

    @Operation(
            summary = "교사 회원 가입",
            description = "관리자가 승인하기 전에는 로그인 불가"
    )
    @PostMapping("join")
    public ResponseEntity<Void> teacherJoin(
            @Valid @RequestBody JoinTeacherRequest joinTeacherRequest
    ) {
        teacherCertService.joinTeacher(joinTeacherRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(
            summary = "교사의 아이디 찾기",
            description = "가입된 이메일이 있을 경우 전송."
    )
    @PostMapping("find/username")
    public ResponseEntity<String> findUsernameByEmail(
            @Valid @RequestBody FindUsernameRequest findUsernameRequest
    ) {
        teacherCertService.findUsernameByEmail(findUsernameRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(
            summary = "비밀번호 찾기(변경)",
            description = "등록된 정보에 있을 경우 레디스에 인증번호 저장한 뒤, 이메일로 인증 번호 발송"
    )
    @PostMapping("find/password")
    public ResponseEntity<String> sendPasswordResetEmail(
            @Valid @RequestBody FindPasswordRequest findPasswordRequest
    ) {
        teacherCertService.sendPasswordResetEmail(findPasswordRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(
            summary = "인증번호 유효성 확인",
            description = "유효 시간내 입력한 인증번호가 일치할 경우 레디스에서 삭제한 뒤 승인"
    )
    @PostMapping("find/authNumCheck")
    public ResponseEntity<String> verifyAuthCode(
            @Valid @RequestBody AuthCodeRequest authCodeRequest
    ) {
        authEmailService.verifyAuthCode(authCodeRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "사용자 인증을 거친 후, 변경"
    )
    @PatchMapping("manage/password")
    public ResponseEntity<String> initializePassword(
            @Valid @RequestBody PwdEditRequest pwdEditRequest
    ) {
        teacherCertService.initializePassword(pwdEditRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(
            summary = "이메일 변경",
            description = "교사 이메일 변경"
    )
    @PatchMapping("manage/email")
    public ResponseEntity<Void> changeTeacherEmail(
            @Valid @RequestBody EditEmailRequest editEmailRequest
    ) {
        teacherCertService.changeTeacherEmail(editEmailRequest);
        return new ResponseEntity<>(OK);
    }
}

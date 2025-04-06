package com.scheduler.memberservice.member.student.controller;

import com.scheduler.memberservice.member.student.service.StudentCertService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("student")
@RequiredArgsConstructor
public class StudentCertController {

    private final StudentCertService studentCertService;

    @Operation(
            summary = "학생 회원 가입",
            description = "교사가 정해지지 않아도 가능"
    )
    @PostMapping("join")
    public ResponseEntity<String> saveStudent(
            @Valid @RequestBody RegisterStudentRequest registerStudentRequest
    ) {
        studentCertService.registerStudent(registerStudentRequest);
        return new ResponseEntity<>(OK);
    }

    @Operation(
            summary = "학생 정보 변경",
            description = "대상 : 전화 번호, 이메일, 주소(상세 포함), 보호자 전화번호 "
    )
    @PatchMapping("modify/info")
    public ResponseEntity<String> modifyStudentInfo(
            @Valid @RequestBody ModifyStudentInfoRequest registerStudentRequest
    ) {
        studentCertService.modifyStudentInfo(registerStudentRequest);
        return  new ResponseEntity<>(OK);
    }

    @Operation(
            summary = "비밀 번호 변경",
            description = "로그인 한 상태에서 변경"
    )
    @PatchMapping("modify/password")
    public ResponseEntity<String> modifyStudentPassword(
            @Valid @RequestBody ModifyStudentPasswordRequest modifyStudentPasswordRequest
    ) {
        studentCertService.modifyStudentPassword(modifyStudentPasswordRequest);
        return  new ResponseEntity<>(OK);
    }

}

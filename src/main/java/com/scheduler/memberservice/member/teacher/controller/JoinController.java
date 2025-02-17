package com.scheduler.memberservice.member.teacher.controller;

import com.scheduler.memberservice.member.teacher.application.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.JoinTeacherRequest;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("join")
@RequiredArgsConstructor
public class JoinController {

    private final TeacherService teacherService;

    @Operation(description = "교사 회원 가입")
    @PostMapping
    public ResponseEntity<Void> approved(
            @Valid @RequestBody JoinTeacherRequest joinTeacherRequest
    ) {
        teacherService.joinTeacher(joinTeacherRequest);
        return new ResponseEntity<>(OK);
    }
}

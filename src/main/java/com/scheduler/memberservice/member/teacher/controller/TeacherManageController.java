package com.scheduler.memberservice.member.teacher.controller;

import com.scheduler.memberservice.member.teacher.service.TeacherManageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("manage/teacher")
@RequiredArgsConstructor
public class TeacherManageController {

    private final TeacherManageService teacherManageService;

    @Operation(
            summary = "교사 가입 정보 확인",
            description = "모든 상태 확인(등록, 비등록)"
    )
    @GetMapping("list")
    public ResponseEntity<List<TeacherResponse>> getTeacherList() {
        return new ResponseEntity<>(teacherManageService.getTeacherList(), OK);
    }

    @Operation(
            summary = "특정 교사 정보 조회",
            description = "상세 정보 조회"
    )
    @GetMapping("{username}")
    public ResponseEntity<TeacherResponse> findTeacherInformation(
            @PathVariable String username
    ) {
        return new ResponseEntity<>(teacherManageService.findTeacherInformation(username), OK);
    }

    @Operation(
            summary = "교사 상태 변경",
            description = "승인 비승인 여부 변경"
    )
    @PatchMapping("{username}/status")
    public ResponseEntity<String> changeTeacherStatus(
            @PathVariable String username
    ) {
        teacherManageService.changeTeacherStatus(username);
        return ResponseEntity.ok("승인되었습니다.");
    }
}

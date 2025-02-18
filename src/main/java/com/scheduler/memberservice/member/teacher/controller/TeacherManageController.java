package com.scheduler.memberservice.member.teacher.controller;

import com.scheduler.memberservice.member.teacher.application.TeacherManageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("manage")
@RequiredArgsConstructor
public class TeacherManageController {

    private final TeacherManageService teacherManageService;

    @GetMapping("teacher/list")
    public ResponseEntity<List<TeacherResponse>> getTeacherList() {
        return new ResponseEntity<>(teacherManageService.getTeacherList(), OK);
    }

    @GetMapping("teacher/{username}")
    public ResponseEntity<TeacherResponse> findTeacherInformation(
            @PathVariable String username
    ) {
        return new ResponseEntity<>(teacherManageService.findTeacherInformation(username), OK);
    }

    @PatchMapping("teacher/{username}/status")
    public ResponseEntity<String> changeTeacherStatus(
            @PathVariable String username
    ) {
        teacherManageService.changeTeacherStatus(username);
        return ResponseEntity.ok("승인되었습니다.");
    }

    @PatchMapping("teacher/change")
    public ResponseEntity<String> changeTeacher(
            @Valid @RequestBody ChangeTeacherRequest changeTeacherRequest
    ) {
        teacherManageService.changeExistTeacher(changeTeacherRequest);
        return ResponseEntity.ok("변경되었습니다.");
    }
}

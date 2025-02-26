package com.scheduler.memberservice.member.student.controller;

import com.scheduler.memberservice.member.student.application.StudentManageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeStudentName;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("manage")
@RequiredArgsConstructor
public class StudentManageController {

    private final StudentManageService studentManageService;

    @GetMapping("student/list")
    public ResponseEntity<Page<StudentInfoResponse>> studentList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String teacherName,
            @RequestParam(required = false) String studentName
    ) {
        return new ResponseEntity<>(studentManageService
                .findStudentInfoList(teacherName, studentName, of(page, size)), OK);
    }

    @PatchMapping("{studentId}/status")
    public ResponseEntity<Void> changeStudentStatus(
            @PathVariable String studentId
    ) {
        studentManageService.changeStudentStatus(studentId);
        return new ResponseEntity<>(OK);
    }

    @PatchMapping("teacher/change")
    public ResponseEntity<String> changeTeacher(
            @Valid @RequestBody ChangeTeacherRequest changeTeacherRequest
    ) {
        studentManageService.changeExistTeacher(changeTeacherRequest);
        return ResponseEntity.ok("변경되었습니다.");
    }

    @PatchMapping("student/name")
    public ResponseEntity<String> changeStudentName(
            @Valid @RequestBody ChangeStudentName changeStudentName
    ) {
        studentManageService.changeStudentName(changeStudentName);
        return ResponseEntity.ok("변경되었습니다.");
    }
}

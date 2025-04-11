package com.scheduler.memberservice.member.student.controller;

import com.scheduler.memberservice.member.student.dto.StudentRequest;
import com.scheduler.memberservice.member.student.service.StudentManageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("manage")
@RequiredArgsConstructor
public class StudentManageController {

    private final StudentManageService studentManageService;

    @Operation(
            summary = "전체 학생 정보 확인",
            description = "페이징으로 확인"
    )
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

    @Operation(
            summary = "학생 등록 상태 확인",
            description = "학생 고유 값으로 확인"
    )
    @PatchMapping("student/{studentId}/status")
    public ResponseEntity<Void> changeStudentStatus(
            @PathVariable String studentId
    ) {
        studentManageService.changeStudentStatus(studentId);
        return new ResponseEntity<>(OK);
    }

    @Operation(
            summary = "담당 교사 변경",
            description = "기존 교사와 변경될 교사의 정보를 확인 후, 변경"
    )
    @PatchMapping("student/change")
    public ResponseEntity<String> changeTeacher(
            @Valid @RequestBody ChangeTeacherRequest changeTeacherRequest
    ) {
        studentManageService.changeExistTeacher(changeTeacherRequest);
        return ResponseEntity.ok("변경되었습니다.");
    }

    @Operation(
            summary = "학생 이름 변경",
            description = "rabbitmq를 이용해서 비동기로 수정. 실패시 아웃박스 로직 작동"
    )
    @PatchMapping("student/name")
    public ResponseEntity<String> changeStudentName(
            @Valid @RequestBody StudentRequest.ChangeStudentNameRequest changeStudentNameRequest
    ) {
        studentManageService.changeStudentName(changeStudentNameRequest);
        return ResponseEntity.ok("변경되었습니다.");
    }
}

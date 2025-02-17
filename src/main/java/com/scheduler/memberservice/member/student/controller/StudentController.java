package com.scheduler.memberservice.member.student.controller;

import com.scheduler.memberservice.member.student.application.StudentService;
import com.scheduler.memberservice.member.student.dto.StudentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("join")
    public ResponseEntity<String> saveStudent(
            @Valid @RequestBody RegisterStudentRequest registerStudentRequest
    ) {
        studentService.registerStudent(registerStudentRequest);
        return new ResponseEntity<>(OK);
    }

    @PatchMapping("modify/info")
    public ResponseEntity<String> modifyStudentInfo(
            @Valid @RequestBody StudentRequest.ModifyStudentInfoRequest registerStudentRequest
    ) {
        studentService.modifyStudentInfo(registerStudentRequest);
        return  new ResponseEntity<>(OK);
    }

    @PatchMapping("modify/password")
    public ResponseEntity<String> modifyStudentPassword(
            @Valid @RequestBody StudentRequest.ModifyStudentPasswordRequest modifyStudentPasswordRequest
    ) {
        studentService.modifyStudentPassword(modifyStudentPasswordRequest);
        return  new ResponseEntity<>(OK);
    }

}

package com.scheduler.memberservice.member.student;

import com.scheduler.memberservice.member.student.application.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.ModifyStudentRequest;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.RegisterStudentRequest;
import static com.scheduler.memberservice.member.student.dto.StudentResponse.StudentInfoResponse;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("manage")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("saveStudent")
    public ResponseEntity<String> saveStudent(
            @RequestBody RegisterStudentRequest registerStudentRequest
    ) {
        studentService.registerStudentInformation(registerStudentRequest);
        return new ResponseEntity<>(OK);
    }

    @PatchMapping("modifyStudent")
    public ResponseEntity<String> modifyStudentVerification(
            @RequestBody ModifyStudentRequest registerStudentRequest
    ) {
        studentService.modifyStudentVerification(registerStudentRequest);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @GetMapping("studentList")
    public ResponseEntity<Page<StudentInfoResponse>> studentList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String teacherName,
            @RequestParam(required = false) String studentName
    ) {
        return new ResponseEntity<>(studentService
                .findStudentInfoList(teacherName, studentName, of(page, size)), OK);


    }
}

package com.scheduler.memberservice.member.admin.controller;

import com.scheduler.memberservice.member.admin.application.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
public class AdminController {

    public final AdminService adminService;

    @PatchMapping("grant/{teacherId}")
    public ResponseEntity<String> grantAuth(@PathVariable String teacherId) {
        adminService.grantAuth(teacherId);
        return ResponseEntity.ok("승인되었습니다.");
    }

    @PatchMapping("revoke/{teacherId}")
    public ResponseEntity<String> revokeAuth(@PathVariable String teacherId) {
        adminService.revokeAuth(teacherId);
        return ResponseEntity.ok("승인 취소되었습니다.");
    }

    @PatchMapping("changeTeacher")
    public ResponseEntity<String> changeTeacher(@RequestBody ChangeTeacherRequest changeTeacherRequest) {
        adminService.changeExistTeacher(changeTeacherRequest);
        return ResponseEntity.ok("변경되었습니다.");
    }

    @DeleteMapping("delete/{teacherId}")
    public ResponseEntity<String> delete(@PathVariable String teacherId) {
        adminService.deleteTeacherAccount(teacherId);
        return ResponseEntity.ok("삭제 되었습니다.");
    }

    @GetMapping("teacherList")
    public ResponseEntity<List<TeacherResponse>> teacherList() {
        return new ResponseEntity<>(adminService.getTeacherList(), OK);
    }
}

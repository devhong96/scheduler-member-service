package com.scheduler.memberservice.client.controller;

import com.scheduler.memberservice.client.service.FeignCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/feign-member")
@RequiredArgsConstructor
public class FeignController {

    private final FeignCourseService feignCourseService;

    @GetMapping("/teacher/info")
    public ResponseEntity<TeacherInfo> findTeacherInfoByToken(
            @RequestHeader(AUTHORIZATION) String token
    ){
        return new ResponseEntity<>(feignCourseService.findTeacherInfoByToken(token), OK);
    }

    @GetMapping("/student/info")
    public ResponseEntity<StudentInfo> findStudentInfoByToken(
            @RequestHeader(AUTHORIZATION) String token
    ){
        return new ResponseEntity<>(feignCourseService.findStudentInfoByToken(token), OK);
    }

    @GetMapping("/member/info")
    public ResponseEntity<MemberInfo> findMemberInfoByToken(
            @RequestHeader(AUTHORIZATION) String token
    ){
        return new ResponseEntity<>(feignCourseService.findMemberInfoByToken(token), OK);
    }
}

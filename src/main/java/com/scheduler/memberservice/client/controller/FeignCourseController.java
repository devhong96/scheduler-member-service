package com.scheduler.memberservice.client.controller;

import com.scheduler.memberservice.client.service.FeignCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.MemberInfo;
import static com.scheduler.memberservice.client.dto.FeignMemberResponse.StudentInfo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("feign-member")
@RequiredArgsConstructor
public class FeignCourseController {

    private final FeignCourseService feignCourseService;

    @GetMapping("student/info")
    public ResponseEntity<StudentInfo> findStudentInfoByToken(
            @RequestHeader(AUTHORIZATION) String token
    ){
        return new ResponseEntity<>(feignCourseService.findStudentInfoByToken(token), OK);
    }

    @PostMapping("member/info")
    public  ResponseEntity<MemberInfo> findMemberInfoByToken(
            @RequestHeader(AUTHORIZATION) String token
    ){
        return new ResponseEntity<>(feignCourseService.findMemberInfoByToken(token), OK);
    }
}

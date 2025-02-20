package com.scheduler.memberservice.client.controller;

import com.scheduler.memberservice.client.service.FeignCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("feign-member")
@RequiredArgsConstructor
public class FeignCourseController {

    private final FeignCourseService feignCourseService;

    @PostMapping("student/{username}")
    public ResponseEntity<StudentInfo> findStudentInfoByUsername(
            @RequestHeader("Authorization") String token
    ){
        return new ResponseEntity<>(feignCourseService.findStudentInfoByToken(token), OK);
    }

    @GetMapping("memberInfo")
    public  ResponseEntity<MemberInfo> findMemberInfoByToken(
            @RequestHeader("Authorization") String token
    ){
        return new ResponseEntity<>(feignCourseService.findMemberInfoByToken(token), OK);
    }
}

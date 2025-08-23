package com.scheduler.memberservice.client.controller;

import com.scheduler.memberservice.client.service.FeignOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.StudentResponse;
import static org.springframework.cloud.config.client.ConfigClientProperties.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/feign-order-member")
@RequiredArgsConstructor
public class FeignOrderController {

    private final FeignOrderService feignOrderService;

    @GetMapping("student/info")
    public ResponseEntity<StudentResponse> getStudentInfo(
            @RequestHeader(AUTHORIZATION) String accessToken
    ){
        return new ResponseEntity<>(feignOrderService.getStudentInfo(accessToken), OK);
    }

    @GetMapping("student/{username}")
    public ResponseEntity<StudentResponse> findStudentByUsername(@PathVariable String username
    ) {
        return new ResponseEntity<>(feignOrderService.findStudentByUsername(username), OK);
    }

}

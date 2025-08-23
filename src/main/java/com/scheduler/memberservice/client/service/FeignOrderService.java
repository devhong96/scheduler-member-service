package com.scheduler.memberservice.client.service;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.StudentResponse;

public interface FeignOrderService {

    StudentResponse getStudentInfo(String accessToken);

    StudentResponse findStudentByUsername(String username);

}

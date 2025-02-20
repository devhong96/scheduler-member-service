package com.scheduler.memberservice.client.service;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.MemberInfo;
import static com.scheduler.memberservice.client.dto.FeignMemberResponse.StudentInfo;

public interface FeignCourseService {

    StudentInfo findStudentInfoByToken(String token);

    MemberInfo findMemberInfoByToken(String token);
}

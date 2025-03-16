package com.scheduler.memberservice.client.service;

import static com.scheduler.memberservice.client.dto.FeignMemberResponse.*;

public interface FeignCourseService {

    TeacherInfo findTeacherInfoByToken(String token);

    StudentInfo findStudentInfoByToken(String token);

    MemberInfo findMemberInfoByToken(String token);
}

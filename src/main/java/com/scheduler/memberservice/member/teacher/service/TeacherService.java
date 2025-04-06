package com.scheduler.memberservice.member.teacher.service;

import com.scheduler.memberservice.member.teacher.domain.Teacher;

public interface TeacherService {

    Teacher findTeacherByUsernameIs(String username);

}

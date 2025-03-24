package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.member.teacher.domain.Teacher;

public interface TeacherService {

    Teacher findTeacherByUsernameIs(String username);

}

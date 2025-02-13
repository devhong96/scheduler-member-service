package com.scheduler.memberservice.member.teacher.application;

import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.JoinTeacherRequest;


public interface TeacherService {

    void joinTeacher(JoinTeacherRequest joinTeacherRequest);
}

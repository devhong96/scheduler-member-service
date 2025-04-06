package com.scheduler.memberservice.member.teacher.service;

import java.util.List;

import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.TeacherResponse;

public interface TeacherManageService {

    List<TeacherResponse> getTeacherList();

    TeacherResponse findTeacherInformation(String username);

    void changeTeacherStatus(String username);
}

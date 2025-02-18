package com.scheduler.memberservice.member.teacher.application;

import java.util.List;

import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoResponse.*;

public interface TeacherManageService {

    List<TeacherResponse> getTeacherList();

    TeacherResponse findTeacherInformation(String username);

    void changeExistTeacher(ChangeTeacherRequest changeTeacherRequest);

    void changeTeacherStatus(String teacherId);
}

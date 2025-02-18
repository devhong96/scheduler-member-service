package com.scheduler.memberservice.member.teacher.application;

import com.scheduler.memberservice.infra.IntegrationTest;
import com.scheduler.memberservice.infra.exception.custom.MemberExistException;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.scheduler.memberservice.infra.TestConstants.*;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.JoinTeacherRequest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
class TeacherServiceTest {

}
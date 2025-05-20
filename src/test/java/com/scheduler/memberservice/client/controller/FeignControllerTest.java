package com.scheduler.memberservice.client.controller;

import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.student.WithStudent;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class FeignControllerTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithTeacher(username = "lee_teacher")
    void findTeacherInfoByToken() throws Exception {

        mockMvc.perform(get("/feign-member/teacher/info")
                        .header(AUTHORIZATION, getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    @WithStudent(username = "lee_student")
    void findStudentInfoByToken() throws Exception {

        mockMvc.perform(get("/feign-member/student/info")
                        .header(AUTHORIZATION, getAccessToken()))
                .andExpect(status().isOk());

    }

    @Test
    @WithTeacher(username = "lee_teacher")
    void findMemberInfoByToken() throws Exception {

        mockMvc.perform(get("/feign-member/member/info")
                        .header(AUTHORIZATION, getAccessToken()))
                .andExpect(status().isOk());
    }

    private String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authentication);
        return "Bearer " + jwtTokenDto.getAccessToken();
    }
}
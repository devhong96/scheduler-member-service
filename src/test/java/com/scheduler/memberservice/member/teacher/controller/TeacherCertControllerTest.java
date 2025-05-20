package com.scheduler.memberservice.member.teacher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.teacher.WithTeacher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION;
import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindPasswordRequest;
import static com.scheduler.memberservice.infra.email.dto.FindInfoRequest.FindUsernameRequest;
import static com.scheduler.memberservice.member.teacher.dto.TeacherInfoRequest.*;
import static com.scheduler.memberservice.testSet.TestConstants.TEST_TEACHER_EMAIL;
import static com.scheduler.memberservice.testSet.TestConstants.TEST_TEACHER_USERNAME;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class TeacherCertControllerTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("교사 컨트롤러 : 회원가입")
    void teacherJoin() throws Exception {

        JoinTeacherRequest request = new JoinTeacherRequest();
        request.setUsername("teacher001");
        request.setPassword("securePass!23");
        request.setTeacherName("김선생");
        request.setEmail("teacher.kim@example.com");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/teacher/join")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("교사 컨트롤러 : 아이디를 이메일로 찾기")
    void findUsernameByEmail() throws Exception {
        FindUsernameRequest request = new FindUsernameRequest();
        request.setEmail(TEST_TEACHER_EMAIL);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/teacher/find/username")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("교사 컨트롤러 : 이메일로 비밀번호 초기화 메일 보내기")
    void sendPasswordResetEmail() throws Exception {
        FindPasswordRequest request = new FindPasswordRequest();

        request.setEmail(TEST_TEACHER_EMAIL);
        request.setUsername(TEST_TEACHER_USERNAME);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/teacher/find/username")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

//    @Test
    void verifyAuthCode() {
    }

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("교사 컨트롤러 : 비밀번호 초기화")
    void initializePassword() throws Exception {
        String accessToken = getAccessToken();

        PwdEditRequest request = new PwdEditRequest();
        request.setNewPassword("newPassword");
        request.setCheckPassword("newPassword");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/teacher/manage/password")
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithTeacher(username = TEST_TEACHER_USERNAME)
    @DisplayName("교사 컨트롤러 : 이메일 변경")
    void changeTeacherEmail() throws Exception {
        String accessToken = getAccessToken();

        EditEmailRequest request = new EditEmailRequest();
        request.setEmail("test@gmail.com");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/teacher/manage/email")
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    private String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authentication);
        return "Bearer " + jwtTokenDto.getAccessToken();
    }
}
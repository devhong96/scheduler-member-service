package com.scheduler.memberservice.member.student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.student.WithStudent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.*;
import static com.scheduler.memberservice.testSet.TestConstants.TEST_STUDENT_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class StudentCertControllerTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("학생 1차 회원가입")
    void saveStudent() throws Exception {

        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setUsername("student001");
        request.setPassword("securePass123!");
        request.setStudentName("홍길동");
        request.setStudentPhoneNumber("010-1234-5678");
        request.setStudentEmail("honggildong@example.com");
        request.setStudentAddress("서울특별시 강남구 테헤란로 123");
        request.setStudentDetailedAddress("101동 202호");
        request.setStudentParentPhoneNumber("010-9876-5432");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/student/join")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

    }

    @Test
    @WithStudent(username = TEST_STUDENT_NAME)
    @DisplayName("학생 본인의 개인정보 변경")
    void modifyStudentInfo() throws Exception {

        String accessToken = getAccessToken();

        ModifyStudentInfoRequest request = new ModifyStudentInfoRequest();
        request.setStudentPhoneNumber("010-1234-5678");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/student/modify/info")
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithStudent(username = TEST_STUDENT_NAME)
    @DisplayName("학생 본인의 계정 비밀번호 변경")
    void modifyStudentPassword() throws Exception {

        String accessToken = getAccessToken();

        ModifyStudentPasswordRequest request = new ModifyStudentPasswordRequest();
        request.setNewPassword("1234");
        request.setConfirmNewPassword("1234");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/student/modify/password")
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
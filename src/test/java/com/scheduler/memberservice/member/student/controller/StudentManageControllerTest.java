package com.scheduler.memberservice.member.student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.scheduler.memberservice.client.CourseServiceClient;
import com.scheduler.memberservice.infra.security.jwt.component.JwtUtils;
import com.scheduler.memberservice.infra.security.jwt.dto.JwtTokenDto;
import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.admin.WithAdmin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION;
import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseReassignmentResponse;
import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeTeacherRequest;
import static com.scheduler.memberservice.testSet.TestConstants.TEST_ADMIN_USERNAME;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class StudentManageControllerTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    @MockitoBean
    private CourseServiceClient courseServiceClient;
    //== setting

    @BeforeEach
    void startWireMockServer() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }
    }

    @AfterEach
    void stopWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    @DisplayName("학생 리스트 응답")
    void studentList() throws Exception {

        String accessToken = getAccessToken();

        mockMvc.perform(get("/manage/student/list")
                        .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    void changeStudentStatus() throws Exception {

        String accessToken = getAccessToken();

        mockMvc.perform(patch("/manage/student/STU001/status")
                        .header(AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @WithAdmin(username = TEST_ADMIN_USERNAME)
    void changeTeacher() throws Exception {

        when(courseServiceClient.validateStudentCoursesAndReassign("TCH001", "STU002"))
                .thenReturn(new CourseReassignmentResponse(true));

        ChangeTeacherRequest request = new ChangeTeacherRequest();

        request.setTeacherId("TCH001");
        request.setStudentId("STU002");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/manage/student/change")
                        .header(AUTHORIZATION, getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

//    @Test
    void changeStudentName() {
    }

    private String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtTokenDto jwtTokenDto = jwtUtils.generateToken(authentication);
        return "Bearer " + jwtTokenDto.getAccessToken();
    }
}
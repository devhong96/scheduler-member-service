package com.scheduler.memberservice.client.controller;

import com.scheduler.memberservice.testSet.IntegrationTest;
import com.scheduler.memberservice.testSet.student.WithStudent;
import org.junit.jupiter.api.Test;

@IntegrationTest
class FeignOrderControllerTest {

    @Test
    @WithStudent(username = "lee_student")
    void getStudentInfo() {

    }

    @Test
    @WithStudent(username = "lee_student")
    void findStudentByUsername() {
    }
}
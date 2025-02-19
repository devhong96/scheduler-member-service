package com.scheduler.memberservice.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "scheduler-course-service",
        path = "/scheduler-course-service/feign-course/",
        configuration = CourseFeignErrorDecoder.class
)
public interface CourseServiceClient {

    @Operation(summary = "선생님이 담당하는 학생들의 주간 수업 존재 여부 조회")
    @GetMapping("teacher/{teacherId}/courses")
    Boolean existWeeklyCoursesByTeacherId(
            @PathVariable String teacherId
    );

    @Operation(summary = "학생과 선생님의 주간 코스 중복 확인 후, 변경")
    @PatchMapping("teacher/{teacherId}/student/{studentId}")
    Boolean validateStudentCoursesAndReassign(
            @PathVariable String teacherId,
            @PathVariable String studentId
    );
}

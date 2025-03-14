package com.scheduler.memberservice.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseExistenceResponse;
import static com.scheduler.memberservice.client.dto.FeignMemberRequest.CourseReassignmentResponse;

@FeignClient(
        name = "scheduler-course-service",
        url =  "${scheduler_course_service_url:}",
        path = "/feign-course",
        configuration = CourseFeignErrorDecoder.class
)
public interface CourseServiceClient {

    @Operation(summary = "선생님이 담당하는 학생들의 주간 수업 존재 여부 조회")
    @GetMapping("teacher/{teacherId}/courses")
    CourseExistenceResponse existWeeklyCoursesByTeacherId(
            @PathVariable String teacherId
    );

    @Operation(summary = "학생과 선생님의 주간 코스 중복 확인 후, 변경")
    @PatchMapping("teacher/{teacherId}/student/{studentId}")
    CourseReassignmentResponse validateStudentCoursesAndReassign(
            @PathVariable String teacherId,
            @PathVariable String studentId
    );
}

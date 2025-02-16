package com.scheduler.memberservice.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.scheduler.memberservice.client.dto.FeignCourseResponse.StudentCourseResponse;

@FeignClient(
        name = "scheduler-course-service",
        path = "/scheduler-member-service/feign-course/",
        configuration = CourseFeignErrorDecoder.class
)
public interface CourseServiceClient {

    @Operation(summary = "학생의 주간 수업 조회")
    @GetMapping("/student/{studentId}/courses")
    StudentCourseResponse getWeeklyCoursesByStudentId(@PathVariable String studentId);

    @Operation(summary = "학생의 스케줄 삭제")
    @DeleteMapping("/student/{studentId}")
    void deleteScheduleByStudentId(@PathVariable String studentId);

    @Operation(summary = "선생님이 담당하는 학생들의 주간 수업 조회")
    @GetMapping("/teacher/{teacherId}/courses")
    List<StudentCourseResponse> getWeeklyCoursesByTeacherId(@PathVariable String teacherId);

    @Operation(summary = "학생의 코스를 다른 선생님에게 재배정")
    @PatchMapping("/teacher/{teacherId}/student/{studentId}")
    void reassignStudentCourses(@PathVariable String teacherId, @PathVariable String studentId);
}

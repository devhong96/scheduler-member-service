package com.scheduler.memberservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.scheduler.memberservice.client.dto.FeignCourseResponse.StudentCourseResponse;

@FeignClient(name = "course-service", configuration = CourseFeignErrorDecoder.class)
public interface CourseServiceClient {

    @DeleteMapping("{studentId}")
    void deleteScheduleByStudentId(@PathVariable String studentId);

    @GetMapping("{teacherId}")
    List<StudentCourseResponse> getWeeklyCoursesByTeacherId(@PathVariable String teacherId);

    @GetMapping("{studentId}")
    StudentCourseResponse getWeeklyCoursesByStudentId(@PathVariable String studentId);

    @PatchMapping("{teacherId}/{studentId}")
    void reassignStudentCourses(@PathVariable String teacherId, @PathVariable String studentId);
}

package com.scheduler.memberservice.client.dto;

import lombok.Getter;
import lombok.Setter;

public class FeignCourseResponse {

    @Getter
    @Setter
    public static class StudentCourseResponse {

        private Integer mondayClass;
        private Integer tuesdayClass;
        private Integer wednesdayClass;
        private Integer thursdayClass;
        private Integer fridayClass;
        private Integer year;
        private Integer weekNumber;
    }
}

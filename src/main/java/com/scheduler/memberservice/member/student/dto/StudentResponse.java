package com.scheduler.memberservice.member.student.dto;

import lombok.Getter;
import lombok.Setter;

public class StudentResponse {

    @Getter
    @Setter
    public static class StudentInfoResponse {

        private String studentName;

        private String teacherName;

    }

    @Getter
    @Setter
    public static class StudentAddressResponse {

        private String studentPhoneNumber;

        private String studentAddress;

        private String studentDetailedAddress;

        private String studentParentPhoneNumber;

    }
}

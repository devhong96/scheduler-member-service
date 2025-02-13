package com.scheduler.memberservice.member.student.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class StudentRequest {

    @Getter
    @Setter
    public static class RegisterStudentRequest {

        @NotEmpty(message = "학생 이름을 정확히 입력해 주세요")
        private String studentName;

        private String password;

        @NotEmpty(message = "학생 전화번호를 입력해 주세요")
        private String studentPhoneNumber;

        @NotEmpty(message = "주소를 입력해 주세요")
        private String studentAddress;

        @NotEmpty(message = "상세주소를 입력해 주세요")
        private String studentDetailedAddress;

        @NotEmpty(message = "학부모 전화번호를 입력해 주세요")
        private String studentParentPhoneNumber;

        private String teacherName;

        private String teacherUsername;

        public void setStudentPhoneNumber(String studentPhoneNumber) {
            this.studentPhoneNumber = studentPhoneNumber.replace("-", "");
        }

        public void setStudentParentPhoneNumber(String studentParentPhoneNumber) {
            this.studentParentPhoneNumber = studentParentPhoneNumber.replace("-", "");
        }
    }

    @Getter
    @Setter
    public static class ModifyStudentRequest {
        private String studentId;
        private Boolean isApproved;
    }
}

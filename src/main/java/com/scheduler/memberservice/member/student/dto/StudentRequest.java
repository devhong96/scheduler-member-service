package com.scheduler.memberservice.member.student.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StudentRequest {

    @Getter
    @Setter
    public static class RegisterStudentRequest {

        @JsonIgnore
        private String studentId;

        @NotEmpty(message = "아이디를 입력해주세요")
        private String username;

        @NotEmpty(message = "비밀번호를 정확히 입력해 주세요")
        private String password;

        @NotEmpty(message = "학생 이름을 정확히 입력해 주세요")
        private String studentName;

        @NotEmpty(message = "학생 전화번호를 입력해 주세요")
        private String studentPhoneNumber;

        @Email
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String studentEmail;

        @NotEmpty(message = "주소를 입력해 주세요")
        private String studentAddress;

        @NotEmpty(message = "상세주소를 입력해 주세요")
        private String studentDetailedAddress;

        @NotEmpty(message = "학부모 전화번호를 입력해 주세요")
        private String studentParentPhoneNumber;
    }

    @Getter
    @Setter
    public static class ModifyStudentInfoRequest {

        private String studentPhoneNumber;

        private String studentEmail;

        private String studentAddress;

        private String studentDetailedAddress;

        private String studentParentPhoneNumber;
    }

    @Getter
    @Setter
    public static class ModifyStudentPasswordRequest {

        @NotEmpty(message = "비밀번호를 정확히 입력해 주세요")
        private String newPassword;

        @NotEmpty(message = "비밀번호를 정확히 입력해 주세요")
        private String confirmNewPassword;
    }

    @Getter
    @Setter
    public static class ModifyStudentRequest {
        private String studentId;
        private Boolean isApproved;
    }

    @Getter
    @Setter
    public static class ChangeTeacherRequest {

        private String teacherId;
        private String studentId;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeStudentName {

        private String studentId;
        private String studentName;

    }
}

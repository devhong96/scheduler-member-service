package com.scheduler.memberservice.member.admin.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class AdminInfoRequest {

    @Getter
    @Setter
    public static class EditEmailRequest {

        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;
    }

    @Getter
    @Setter
    public static class ChangeTeacherRequest {

        private String teacherId;
        private String studentId;
    }

    @Getter
    @Setter
    public static class CertDTO {

        private String username;

        @NotEmpty(message = "인증번호를 입력해 주세요")
        private String authNum;
    }

    @Getter
    @Setter
    public static class EmailRequest {

        private String email;

    }

    @Getter
    @Setter
    public static class PwdEditRequest {

        @NotEmpty(message = "변경할 비밀번호를 입력해 주세요.")
        private String password;
    }

}

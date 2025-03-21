package com.scheduler.memberservice.member.teacher.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class TeacherInfoRequest {

    @Getter
    @Setter
    public static class JoinTeacherRequest {

        @JsonIgnore
        private String teacherId;

        @NotEmpty(message = "아이디를 입력해 주세요")
        private String username;

        @NotEmpty(message = "비밀번호를 입력해 주세요")
        private String password;

        @NotEmpty(message = "이름을 입력해 주세요")
        private String teacherName;

        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;
    }

    @Getter
    @Setter
    public static class PwdEditRequest {

        @NotEmpty(message = "변경할 비밀번호를 입력해 주세요.")
        private String newPassword;

        @NotEmpty
        private String checkPassword;
    }

    @Getter
    @Setter
    public static class EditEmailRequest {

        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;
    }

}

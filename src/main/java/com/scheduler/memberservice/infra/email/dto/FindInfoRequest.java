package com.scheduler.memberservice.infra.email.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class FindInfoRequest {

    @Getter
    @Setter
    public static class FindPasswordRequest {

        @NotEmpty(message = "이메일을 입력해 주세요")
        @Email(message = "유효한 이메일 형식이어야 합니다")
        private String email;

        @NotEmpty(message = "아이디을 입력해 주세요")
        private String username;
    }

    @Getter
    @Setter
    public static class FindUsernameRequest {

        @NotEmpty(message = "이메일을 입력해주세요")
        @Email(message = "유효한 이메일 형식이어야 합니다")
        private String email;

        @JsonIgnore
        private String username;
    }

    @Getter
    @Setter
    public static class SendAuthNumDto {

        private String title;

        @Email
        @NotEmpty
        private String email;

        public SendAuthNumDto(String title, String email) {
            this.title = title;
            this.email = email;
        }
    }


    @Getter
    @Setter
    public static class AuthCodeRequest {

        @Email
        @NotEmpty
        private String email;

        @NotEmpty(message = "인증번호를 입력해 주세요")
        private String authNum;

        public AuthCodeRequest(String email, String authNum) {
            this.email = email;
            this.authNum = authNum;
        }
    }
}

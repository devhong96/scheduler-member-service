package com.scheduler.memberservice.member.teacher.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class TeacherInfoResponse {

    @Getter
    @Setter
    public static class TeacherResponse {

        private Long id;

        private String username;

        private String password;

        private String teacherName;

        private String email;

        private String role;

        private Boolean approved;

    }

    @Getter
    @Setter
    public static class EmailDTO {

        private String username;

        private String email;

        @Builder
        public EmailDTO(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }

}

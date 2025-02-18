package com.scheduler.memberservice.member.teacher.dto;

import com.scheduler.memberservice.member.common.RoleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.EnumType.*;

public class TeacherInfoResponse {

    @Getter
    @Setter
    public static class TeacherResponse {

        private Long id;

        private String username;

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

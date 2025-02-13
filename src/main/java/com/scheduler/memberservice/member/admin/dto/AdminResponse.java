package com.scheduler.memberservice.member.admin.dto;

import lombok.Getter;
import lombok.Setter;

public class AdminResponse {

    @Getter
    @Setter
    public static class EmailResponse {

        private String username;

        private String email;

    }
}

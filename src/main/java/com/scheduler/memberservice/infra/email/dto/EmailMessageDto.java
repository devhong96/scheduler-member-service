package com.scheduler.memberservice.infra.email.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailMessageDto {
    private String from;
    private String to;
    private String subject;
    private String html;
    private String plain;
}

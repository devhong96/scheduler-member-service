package com.scheduler.memberservice.infra.email.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class EmailMessageDto {
    private String from;
    private String to;
    private String subject;
    private String message;
}

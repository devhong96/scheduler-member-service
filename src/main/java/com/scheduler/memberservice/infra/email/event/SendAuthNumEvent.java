package com.scheduler.memberservice.infra.email.event;

import com.scheduler.memberservice.infra.email.dto.EmailMessageDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SendAuthNumEvent extends ApplicationEvent {

    private final String from;
    private final String to;
    private final String subject;
    private final String message;

    public SendAuthNumEvent(EmailMessageDto emailMessageDto) {
        super(emailMessageDto);
        this.from = emailMessageDto.getFrom();
        this.to = emailMessageDto.getTo();
        this.subject = emailMessageDto.getSubject();
        this.message = emailMessageDto.getMessage();
    }
}

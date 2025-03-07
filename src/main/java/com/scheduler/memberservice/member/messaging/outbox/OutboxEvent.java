package com.scheduler.memberservice.member.messaging.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.memberservice.infra.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.scheduler.memberservice.member.messaging.outbox.EventType.STUDENT;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class OutboxEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private EventType eventType;

    private String payload;

    private Boolean processed;

    public void updateProcessed(Boolean processed) {
        this.processed = processed;
    }

    public static OutboxEvent create(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.eventType = STUDENT;
        outboxEvent.payload = objectMapper.writeValueAsString(o);
        outboxEvent.processed = false;
        return outboxEvent;
    }
}

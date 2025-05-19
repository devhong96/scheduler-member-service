package com.scheduler.memberservice.infra.messaging.outbox;

import com.scheduler.memberservice.member.student.domain.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.scheduler.memberservice.member.student.dto.StudentRequest.ChangeStudentNameRequest;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class NameOutboxEvent {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private EventType eventType;

    private String memberId;

    private String oldName;

    private String newName;

    private Boolean processed;

    public void updateProcessed(Boolean processed) {
        this.processed = processed;
    }

    public static NameOutboxEvent createStudentOutboxEvent(
            EventType eventType, Student student, ChangeStudentNameRequest changeStudentNameRequest) {
        NameOutboxEvent nameOutboxEvent = new NameOutboxEvent();
        nameOutboxEvent.eventType = eventType;
        nameOutboxEvent.memberId = student.getStudentId();
        nameOutboxEvent.oldName = student.getStudentName();
        nameOutboxEvent.newName = changeStudentNameRequest.getStudentName();
        nameOutboxEvent.processed = false;
        return nameOutboxEvent;
    }
}

package com.scheduler.memberservice.member.student.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scheduler.memberservice.member.messaging.outbox.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeStudentNameRequest {

    private EventType eventType;

    private String memberId;

    private String oldName;

    private String newName;

    private Boolean processed;


}

package com.scheduler.memberservice.member.messaging.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxEventJpaRepository extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findOutboxEventsByProcessedFalse();

    List<OutboxEvent> findOutboxEventsByProcessedFalseAndEventType(EventType eventType);
}

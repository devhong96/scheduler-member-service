package com.scheduler.memberservice.member.messaging.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
public interface OutBoxEventJpaRepository extends JpaRepository<NameOutboxEvent, Long> {

    @Lock(PESSIMISTIC_WRITE)
    List<NameOutboxEvent> findOutboxEventsByProcessedFalseAndEventType(EventType eventType, Pageable pageable);
}

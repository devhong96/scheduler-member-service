package com.scheduler.memberservice.member.messaging.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.EXCHANGE_NAME;
import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.ROUTING_KEY;
import static com.scheduler.memberservice.member.messaging.outbox.EventType.STUDENT;
import static com.scheduler.memberservice.member.messaging.outbox.EventType.TEACHER;
import static org.springframework.data.domain.PageRequest.of;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutBoxEventJpaRepository outboxEventJpaRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 5000)
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    @Transactional
    public void processStudentOutboxEvents() {

        // for. 하나의 컨테이너에서 모든 메시지를 처리하는 것을 방지하기 위함.
        for (OutboxEvent event :
                outboxEventJpaRepository.findOutboxEventsByProcessedFalseAndEventType(STUDENT, of(0, 5))
        ) {
            try {
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event.getPayload());
                event.updateProcessed(true);
                outboxEventJpaRepository.save(event);
            } catch (Exception e) {
                log.error("Failed to send to RabbitMQ: {}", event.getId(), e);
            }
        }
    }

    @Scheduled(fixedRate = 5000)
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    @Transactional
    public void processTeacherOutboxEvents() {

        // for. 하나의 컨테이너에서 모든 메시지를 처리하는 것을 방지하기 위함.
        for (OutboxEvent event :
                outboxEventJpaRepository.findOutboxEventsByProcessedFalseAndEventType(TEACHER, of(0, 5))
        ) {
            try {
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event.getPayload());
                event.updateProcessed(true);
                outboxEventJpaRepository.save(event);
            } catch (Exception e) {
                log.error("Failed to send to RabbitMQ: {}", event.getId(), e);
            }
        }
    }
}

package com.scheduler.memberservice.member.messaging.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.EXCHANGE_NAME;
import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.ROUTING_KEY;
import static com.scheduler.memberservice.member.messaging.outbox.EventType.STUDENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutBoxEventJpaRepository outboxEventJpaRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 5000)
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public void processOutboxEvents() {

        for (OutboxEvent event :
                outboxEventJpaRepository.findOutboxEventsByProcessedFalseAndEventType(STUDENT)
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

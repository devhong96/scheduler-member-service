package com.scheduler.memberservice.member.messaging.outbox;

import com.scheduler.memberservice.infra.config.messaging.RabbitStudentNameProperties;
import com.scheduler.memberservice.infra.config.messaging.RabbitTeacherNameProperties;
import com.scheduler.memberservice.member.student.domain.Student;
import com.scheduler.memberservice.member.student.repository.StudentJpaRepository;
import com.scheduler.memberservice.member.teacher.domain.Teacher;
import com.scheduler.memberservice.member.teacher.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.scheduler.memberservice.member.messaging.outbox.EventType.STUDENT;
import static com.scheduler.memberservice.member.messaging.outbox.EventType.TEACHER;
import static org.springframework.data.domain.PageRequest.of;

@Slf4j
@Service
@RequiredArgsConstructor
public class NameOutboxProcessor {

    private final OutBoxEventJpaRepository outboxEventJpaRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitStudentNameProperties studentProperties;
    private final RabbitTeacherNameProperties teacherProperties;
    private final StudentJpaRepository studentJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;

    @Scheduled(fixedRate = 5000)
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    @Transactional
    public void processStudentOutboxEvents() {

        // for. 하나의 컨테이너에서 모든 메시지를 처리하는 것을 방지하기 위함.
        for (NameOutboxEvent event :
                outboxEventJpaRepository.findOutboxEventsByProcessedFalseAndEventType(STUDENT, of(0, 5))
        ) {
            try {
                rabbitTemplate.convertAndSend(
                        studentProperties.getExchange().getName(),
                        studentProperties.getRouting().getKey(), event);

                event.updateProcessed(true);
            } catch (Exception e) {
                log.error("Failed to send to RabbitMQ: {}", event.getId(), e);
                Student student = studentJpaRepository.findStudentByStudentId(event.getMemberId())
                        .orElseThrow(NoSuchElementException::new);

                student.updateStudentName(event.getOldName());
                throw e; // 재시도를 위함.
            }
        }
    }

    @Scheduled(fixedRate = 5000)
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    @Transactional
    public void processTeacherOutboxEvents() {

        // for. 하나의 컨테이너에서 모든 메시지를 처리하는 것을 방지하기 위함.
        for (NameOutboxEvent event :
                outboxEventJpaRepository.findOutboxEventsByProcessedFalseAndEventType(TEACHER, of(0, 5))
        ) {
            try {
                rabbitTemplate.convertAndSend(teacherProperties.getExchange().getName(),
                        teacherProperties.getRouting().getKey(), event);
                event.updateProcessed(true);
            } catch (Exception e) {
                log.error("Failed to send to RabbitMQ: {}", event.getId(), e);

                Teacher teacher = teacherJpaRepository.findTeacherByTeacherId(event.getMemberId())
                        .orElseThrow();

                teacher.updateTeacherName(event.getOldName());
                throw e; // 재시도를 위함.
            }
        }
    }

    @Transactional
    @RabbitListener(queues = "${spring.rabbitmq.student-name.queue.compensation}")
    public void handleCompensation(NameOutboxEvent compensationEvent) {
        Student student = studentJpaRepository.findStudentByStudentId(compensationEvent.getMemberId())
                .orElseThrow(NoSuchElementException::new);
        student.updateStudentName(compensationEvent.getOldName());
        log.info("Compensated event: {} with old name: {}", compensationEvent.getId(), compensationEvent.getOldName());
    }
}

package com.scheduler.memberservice.messaging;

import com.scheduler.memberservice.member.student.application.ChangeStudentNameRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.TestComponent;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.QUEUE_NAME;

@TestComponent
public class TestRabbitConsumer {

    private final ObjectMapper objectMapper =  new ObjectMapper();
    private final CountDownLatch latch = new CountDownLatch(1);

    private ChangeStudentNameRequest changeStudentName;

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(String payload) throws IOException {
        changeStudentName = objectMapper.readValue(payload, ChangeStudentNameRequest.class);

        latch.countDown();
    }

    public ChangeStudentNameRequest getReceivedMessage() throws InterruptedException {
        latch.await();
        return changeStudentName;
    }
}

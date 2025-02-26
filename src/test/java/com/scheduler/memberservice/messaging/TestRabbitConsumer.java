package com.scheduler.memberservice.messaging;

import com.scheduler.memberservice.member.student.application.ChangeStudentNameRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.TestComponent;

import java.util.concurrent.CountDownLatch;

import static com.scheduler.memberservice.infra.config.messaging.RabbitConfig.QUEUE_NAME;


@TestComponent
public class TestRabbitConsumer {

    private ChangeStudentNameRequest changeStudentName;
    private final CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(ChangeStudentNameRequest changeStudentName) {
        this.changeStudentName = changeStudentName;
        latch.countDown();
    }

    public ChangeStudentNameRequest getReceivedMessage() throws InterruptedException {
        latch.await();
        return changeStudentName;
    }
}

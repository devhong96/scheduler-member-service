package com.scheduler.memberservice.messaging;

import com.scheduler.memberservice.member.student.application.ChangeStudentNameRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

import static com.scheduler.memberservice.messaging.RabbitConfig.QUEUE_NAME;


@Component
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

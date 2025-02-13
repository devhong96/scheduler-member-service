package com.scheduler.memberservice.infra.email.event;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Async
@Slf4j
@Component
@RequiredArgsConstructor
public class SendUsernameEventListener {

    private final JavaMailSender javaMailSender;

    @TransactionalEventListener
    public void handleSendUsernameByEmailEvent(SendEmailEvent sendEmailEvent) {

        String from = sendEmailEvent.getFrom();
        String to = sendEmailEvent.getTo();
        String subject = sendEmailEvent.getSubject();
        String message = sendEmailEvent.getMessage();

        sendEmail(from, to, subject, message);
    }

    @TransactionalEventListener
    public void handleSendAuthNumByEmailEvent(SendAuthNumEvent sendAuthNumEvent) {

        String from = sendAuthNumEvent.getFrom();
        String to = sendAuthNumEvent.getTo();
        String subject = sendAuthNumEvent.getSubject();
        String message = sendAuthNumEvent.getMessage();

        sendEmail(from, to, subject, message);

    }

    private void sendEmail(String from, String to, String subject, String message) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setFrom(from);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error("failed to send email = {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

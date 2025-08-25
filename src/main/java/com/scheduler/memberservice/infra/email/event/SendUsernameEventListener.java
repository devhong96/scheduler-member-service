package com.scheduler.memberservice.infra.email.event;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendUsernameEventListener {

    private final JavaMailSender javaMailSender;

    @Async
    @EventListener
    public void handleSendUsernameByEmailEvent(SendEmailEvent sendEmailEvent) {

        String from = sendEmailEvent.getFrom();
        String to = sendEmailEvent.getTo();
        String subject = sendEmailEvent.getSubject();
        String plain = sendEmailEvent.getPlain();
        String html = sendEmailEvent.getHtml();

        sendEmail(from, to, subject, plain, html);
    }

    public void sendEmail(String from, String to, String subject, String plain, String html) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            mimeMessageHelper.setFrom(from);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(plain, html);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error("failed to send email = {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

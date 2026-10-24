
package com.example.emailservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Value("${MAIL_FROM:${spring.mail.username}}")
    private String fromAddress;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(FileUploadedEvent event) {
        SimpleMailMessage email = new SimpleMailMessage();

        email.setFrom(fromAddress);
        email.setTo(event.recipientEmail());
        email.setSubject("File uploaded successfully");
        email.setText(event.message());

        mailSender.send(email);
    }
}

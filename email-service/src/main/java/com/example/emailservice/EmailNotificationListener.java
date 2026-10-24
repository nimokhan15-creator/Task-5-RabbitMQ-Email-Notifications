
package com.example.emailservice;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationListener {

    private final EmailSenderService emailSenderService;

    public EmailNotificationListener(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receive(FileUploadedEvent event) {
        System.out.println("Received email event for: "
                + event.filename());

        emailSenderService.send(event);

        System.out.println("Email sent to: "
                + event.recipientEmail());
    }
}

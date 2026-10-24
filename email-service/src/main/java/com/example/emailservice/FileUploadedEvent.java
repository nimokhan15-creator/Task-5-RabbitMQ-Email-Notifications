
package com.example.emailservice;

public record FileUploadedEvent(
        String filename,
        String recipientEmail,
        String message
) {}

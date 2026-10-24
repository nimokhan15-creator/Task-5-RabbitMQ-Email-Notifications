
package com.example.storage.event;

public record FileUploadedEvent(
        String filename,
        String recipientEmail,
        String message
) {}

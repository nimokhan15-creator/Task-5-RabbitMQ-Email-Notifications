package com.example.storage;

public record FileUploadedEvent(
        String filename,
        String recipientEmail,
        String message
) {}
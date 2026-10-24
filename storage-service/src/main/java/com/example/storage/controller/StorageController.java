
package com.example.storage.controller;

import com.example.storage.EmailEventPublisher;
import com.example.storage.FileUploadedEvent;
import com.example.storage.entity.StoredData;
import com.example.storage.repository.StoredDataRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class StorageController {

    private final StoredDataRepository storedDataRepository;
    private final EmailEventPublisher emailEventPublisher;

    @Value("${notification.recipient:}")
    private String defaultRecipient;

    private final Path storageLocation = Paths.get("uploads");

    public StorageController(
            StoredDataRepository storedDataRepository,
            EmailEventPublisher emailEventPublisher) {
        this.storedDataRepository = storedDataRepository;
        this.emailEventPublisher = emailEventPublisher;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "recipientEmail", required = false)
            String recipientEmail) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Please select a file to upload.");
        }

        Files.createDirectories(storageLocation);

        String originalFilename = file.getOriginalFilename();

        String filename = Path.of(
                originalFilename == null || originalFilename.isBlank()
                        ? "uploaded-file"
                        : originalFilename
        ).getFileName().toString();

        Path filePath = storageLocation.resolve(filename).normalize();

        if (!filePath.startsWith(storageLocation.normalize())) {
            return ResponseEntity.badRequest()
                    .body("Invalid filename.");
        }

        Files.write(filePath, file.getBytes());

        String recipient = recipientEmail;

        if (recipient == null || recipient.isBlank()) {
            recipient = defaultRecipient;
        }

        if (recipient != null && !recipient.isBlank()) {
            FileUploadedEvent event = new FileUploadedEvent(
                    filename,
                    recipient,
                    "Your file '" + filename
                            + "' was uploaded successfully."
            );

            emailEventPublisher.publish(event);
        }

        return ResponseEntity.ok(
                "File uploaded successfully: " + filename
        );
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String filename) throws IOException {

        Path normalizedStorage = storageLocation.toAbsolutePath().normalize();
        Path filePath = normalizedStorage.resolve(filename).normalize();

        if (!filePath.startsWith(normalizedStorage)
                || !Files.exists(filePath)
                || !Files.isRegularFile(filePath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] fileContent = Files.readAllBytes(filePath);
        return ResponseEntity.ok(fileContent);
    }

    @PostMapping("/data")
    public ResponseEntity<StoredData> saveData(
            @RequestBody StoredData data) {

        StoredData savedData = storedDataRepository.save(data);

        return ResponseEntity.ok(savedData);
    }
}

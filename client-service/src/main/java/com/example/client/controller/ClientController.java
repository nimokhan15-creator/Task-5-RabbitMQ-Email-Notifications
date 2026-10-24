
package com.example.client.controller;

import com.example.client.feign.StorageClient;
import service.StorageGatewayService;import feign.FeignException;
import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/client")
public class ClientController {

    private final StorageClient storageClient;
    private final StorageGatewayService storageGatewayService;

    public ClientController(
            StorageClient storageClient,
            StorageGatewayService storageGatewayService) {
        this.storageClient = storageClient;
        this.storageGatewayService = storageGatewayService;
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<?> downloadFile(
            @PathVariable String filename) {


        try {
            byte[] file = storageGatewayService.downloadFile(filename);
            return ResponseEntity.ok(file);

        } catch (feign.FeignException e) {
            int status = e.status();

            if (status > 0) {
                return ResponseEntity
                        .status(status)
                        .body("Storage service returned an error.");
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body("Storage service could not be reached.");

        } catch (org.springframework.web.server.ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getReason());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }

    }

    @PostMapping("/data")
    public StorageClient.StoredDataResponse saveData(
            @RequestBody StorageClient.StoredDataRequest data) {
        return storageClient.saveData(data);
    }
}

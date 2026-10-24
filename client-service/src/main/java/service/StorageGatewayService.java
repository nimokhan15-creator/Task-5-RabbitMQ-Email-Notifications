
package service;

import com.example.client.feign.StorageClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


public class StorageGatewayService {





        private final StorageClient storageClient;

        public StorageGatewayService(StorageClient storageClient) {
            this.storageClient = storageClient;
        }

        @CircuitBreaker(
                name = "storageService",
                fallbackMethod = "storageFallback"
        )
        public byte[] downloadFile(String filename) {
            return storageClient.downloadFile(filename);
        }

    private byte[] storageFallback(String filename, Throwable exception) {
        if (exception instanceof feign.FeignException.NotFound) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "File not found: " + filename
            );
        }

        throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                "Storage service is currently unavailable"
        );

        }
    }



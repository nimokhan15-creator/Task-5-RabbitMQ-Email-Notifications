package com.example.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "storage-service", url = "http://storage-service:8082", configuration = FeignClient.class)


public interface StorageClient {


    @GetMapping("/files/download/{filename}")
    byte[] downloadFile(@PathVariable("filename") String filename);

    @PostMapping("/files/data")
    StoredDataResponse saveData(@RequestBody StoredDataRequest data);

    record StoredDataRequest(String content) {
    }

    record StoredDataResponse(Long id, String content) {
    }
}
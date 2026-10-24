package com.example.storage.repository;

import com.example.storage.entity.StoredData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredDataRepository extends JpaRepository<StoredData, Long> {
}
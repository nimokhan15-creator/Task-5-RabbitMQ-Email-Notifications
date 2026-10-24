package com.example.storage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stored_data")
public class StoredData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    public StoredData() {
    }

    public StoredData(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
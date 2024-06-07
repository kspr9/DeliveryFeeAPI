package com.fujitsu.delivery_fee_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;

@Entity
@Table(name = "api_response_logs")
public class ApiResponseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob  
    @Column(name = "http_response", nullable = false)
    private String httpResponse;

    // Default constructor
    public ApiResponseLog() {
    }

    // Parameterized constructor
    public ApiResponseLog(String httpResponse) {
        this.httpResponse = httpResponse;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(String httpResponse) {
        this.httpResponse = httpResponse;
    }
}

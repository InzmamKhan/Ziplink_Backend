package com.github.inzmamkhan.ziplink_backend.dto.response;

import java.time.ZonedDateTime;

public class ErrorResponse {

    private int status;
    private String message;
    private ZonedDateTime timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message, ZonedDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
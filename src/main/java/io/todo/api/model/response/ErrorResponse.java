package io.todo.api.model.response;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ErrorResponse {
    private String errorMessage;
    private LocalDateTime timestamp;

    public ErrorResponse(String errorMessage) {
        this();
        this.errorMessage = errorMessage;
    }

    public ErrorResponse() {
        timestamp = LocalDateTime.now(ZoneId.systemDefault());
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

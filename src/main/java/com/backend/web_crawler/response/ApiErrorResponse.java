package com.backend.web_crawler.response;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

public record ApiErrorResponse(
        LocalDateTime timeStamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {
    public ApiErrorResponse(LocalDateTime timeStamp, int status, String error, String message, String path, Map<String, String> validationErrors) {
        this.timeStamp = timeStamp != null ? timeStamp : LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path != null ? path : "";
        this.validationErrors = validationErrors != null ? validationErrors : Collections.emptyMap();
    }

    public ApiErrorResponse(LocalDateTime timeStamp, int status, String message) {
        this(timeStamp, status, status >= 500 ? "Internal Server Error" : "Bad Request", message, "", Collections.emptyMap());
    }

    public ApiErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, Collections.emptyMap());
    }

    public ApiErrorResponse(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, "", Collections.emptyMap());
    }
}

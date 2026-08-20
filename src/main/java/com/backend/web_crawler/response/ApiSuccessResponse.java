package com.backend.web_crawler.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiSuccessResponse<T>(
        boolean success,
        String message,
        T payload
) {
    public ApiSuccessResponse(boolean success, String message, T payload) {
        this.success = success;
        this.message = message;
        this.payload = payload;
    }
}

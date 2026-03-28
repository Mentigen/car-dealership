package ru.CarDealership.api.dto;

import java.time.Instant;

public record ApiResponse<T> (
    int status,
    String message,
    T data,
    Instant timestamp
) {
    public ApiResponse(int status, String message, T data) {
        this(status, message, data, Instant.now());
    }
}

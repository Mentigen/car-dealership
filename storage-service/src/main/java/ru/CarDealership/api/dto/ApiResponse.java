package ru.CarDealership.api.dto;

public record ApiResponse<T>(int status, String message, T data) {}

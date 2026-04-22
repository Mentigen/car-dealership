package ru.CarDealership.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TestDriveRequestResponse(
        UUID id,
        UUID clientId,
        UUID carId,
        LocalDateTime startTime,
        String status
) {}

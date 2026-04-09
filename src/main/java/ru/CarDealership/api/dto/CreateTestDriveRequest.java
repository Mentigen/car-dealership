package ru.CarDealership.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateTestDriveRequest {
    @NotNull
    private UUID carId;
    @NotNull @Future
    private LocalDateTime startTime;
}

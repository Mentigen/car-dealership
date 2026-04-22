package ru.CarDealership.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateTestDriveRequest {
    @NotNull
    private UUID carId;

    @NotNull
    @Future
    private LocalDateTime startTime;
}

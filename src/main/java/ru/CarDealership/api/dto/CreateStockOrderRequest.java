package ru.CarDealership.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateStockOrderRequest {
    @NotNull
    private UUID clientId;
    @NotNull
    private UUID carId;
}

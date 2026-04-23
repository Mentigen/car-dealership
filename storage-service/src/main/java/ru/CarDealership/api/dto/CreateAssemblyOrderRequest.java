package ru.CarDealership.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateAssemblyOrderRequest {
    @NotNull
    private UUID sourceOrderId;

    @NotNull
    private UUID traceId;

    @NotBlank
    private String orderType;

    private UUID carId;
    private UUID carConfigurationId;
}

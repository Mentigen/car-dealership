package ru.CarDealership.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateCustomOrderRequest {
    @NotNull
    private UUID carConfigurationId;
}

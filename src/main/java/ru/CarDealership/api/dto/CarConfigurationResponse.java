package ru.CarDealership.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CarConfigurationResponse(
        UUID id,
        CarModelResponse model,
        List<PartResponse> parts,
        BigDecimal totalPrice
) {}

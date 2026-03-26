package ru.CarDealership.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CustomOrderResponse(
        UUID id,
        UUID clientId,
        UUID managerId,
        BigDecimal price,
        String status,
        UUID carConfigurationId
) implements OrderResponse {}

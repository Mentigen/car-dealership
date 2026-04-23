package ru.CarDealership.api.dto;

import ru.CarDealership.domain.assembly.AssemblyOrderStatus;

import java.time.Instant;
import java.util.UUID;

public record AssemblyOrderResponse(
        UUID id,
        UUID sourceOrderId,
        UUID traceId,
        String orderType,
        UUID carId,
        UUID carConfigurationId,
        AssemblyOrderStatus status,
        String failReason,
        Instant createdAt,
        Instant updatedAt
) {}

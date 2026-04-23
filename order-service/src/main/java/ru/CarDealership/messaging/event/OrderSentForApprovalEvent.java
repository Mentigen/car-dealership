package ru.CarDealership.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record OrderSentForApprovalEvent(
        UUID orderId,
        UUID traceId,
        String orderType,
        UUID carId,
        UUID carConfigurationId,
        Instant occurredAt
) {}

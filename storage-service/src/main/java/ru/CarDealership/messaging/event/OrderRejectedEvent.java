package ru.CarDealership.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record OrderRejectedEvent(
        UUID orderId,
        UUID traceId,
        String reason,
        Instant occurredAt
) {}

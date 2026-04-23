package ru.CarDealership.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record OrderApprovedEvent(
        UUID orderId,
        UUID traceId,
        UUID assemblyOrderId,
        Instant occurredAt
) {}

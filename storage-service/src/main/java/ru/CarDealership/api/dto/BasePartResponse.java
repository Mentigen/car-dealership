package ru.CarDealership.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record BasePartResponse(UUID id, String type, BigDecimal price, List<UUID> compatibleModelIds)
        implements PartResponse {}

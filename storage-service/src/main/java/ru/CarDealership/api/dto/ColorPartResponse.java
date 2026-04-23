package ru.CarDealership.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ColorPartResponse(UUID id, String type, BigDecimal price, List<UUID> compatibleModelIds, String color)
        implements PartResponse {}

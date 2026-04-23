package ru.CarDealership.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CarInfoResponse(UUID id, BigDecimal price) {}

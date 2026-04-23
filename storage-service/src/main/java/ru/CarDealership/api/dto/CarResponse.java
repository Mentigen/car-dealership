package ru.CarDealership.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CarResponse(UUID id, CarConfigurationResponse carConfiguration, BigDecimal price) {}

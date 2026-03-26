package ru.CarDealership.api.dto;

import ru.CarDealership.domain.car.TransmissionType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record TransmissionPartResponse(
        UUID id,
        String type,
        BigDecimal price,
        List<UUID> compatibleModelIds,
        TransmissionType transmissionType
) implements PartResponse {}

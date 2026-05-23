package ru.CarDealership.grpc;

import io.grpc.StatusRuntimeException;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.exceptions.ServiceUnavailableException;

import java.math.BigDecimal;
import java.util.UUID;

public class CarProtoMapper {

    static CarResponse toResponse(CarProto p) {
        return new CarResponse(
                UUID.fromString(p.getId()),
                p.getBrand(),
                p.getModelName(),
                BigDecimal.valueOf(p.getPrice()),
                p.getBodyType(),
                p.getFuelType(),
                p.getDriveType(),
                p.getEnginePower(),
                p.getEngineVolume()
        );
    }

    static RuntimeException mapError(StatusRuntimeException e) {
        return switch (e.getStatus().getCode()) {
            case NOT_FOUND -> new EntityNotFoundException(e.getStatus().getDescription());
            case UNAVAILABLE, DEADLINE_EXCEEDED -> new ServiceUnavailableException("StorageService is unavailable");
            default -> new RuntimeException("gRPC error: " + e.getStatus());
        };
    }
}

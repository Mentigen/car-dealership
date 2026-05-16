package ru.CarDealership.grpc;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.exceptions.ServiceUnavailableException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class GrpcCarClient {

    @GrpcClient("storage-service")
    private CarGrpcServiceGrpc.CarGrpcServiceBlockingStub stub;

    public List<CarResponse> getAvailableCars() {
        try {
            GetAvailableCarsResponse response = stub.getAvailableCars(
                    GetAvailableCarsRequest.newBuilder().build()
            );
            return response.getCarsList().stream().map(this::toResponse).toList();
        } catch (StatusRuntimeException e) {
            throw mapError(e);
        }
    }

    public CarResponse getCarById(UUID id) {
        try {
            CarProto proto = stub.getCarById(
                    GetCarByIdRequest.newBuilder().setId(id.toString()).build()
            );
            return toResponse(proto);
        } catch (StatusRuntimeException e) {
            throw mapError(e);
        }
    }

    private CarResponse toResponse(CarProto p) {
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

    private RuntimeException mapError(StatusRuntimeException e) {
        return switch (e.getStatus().getCode()) {
            case NOT_FOUND -> new EntityNotFoundException(e.getStatus().getDescription());
            case UNAVAILABLE, DEADLINE_EXCEEDED -> new ServiceUnavailableException("StorageService is unavailable");
            default -> new RuntimeException("gRPC error: " + e.getStatus());
        };
    }
}

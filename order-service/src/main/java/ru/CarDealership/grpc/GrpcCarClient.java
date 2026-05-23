package ru.CarDealership.grpc;

import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.CarDealership.api.dto.CarResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class GrpcCarClient {

    @GrpcClient("storage-service")
    private CarGrpcServiceGrpc.CarGrpcServiceBlockingStub stub;

    public List<CarResponse> getAvailableCars() {
        log.info("Calling StorageService.getAvailableCars");
        try {
            GetAvailableCarsResponse response = stub.getAvailableCars(
                    GetAvailableCarsRequest.newBuilder().build()
            );
            log.info("StorageService returned {} cars", response.getCarsList().size());
            return response.getCarsList().stream().map(CarProtoMapper::toResponse).toList();
        } catch (StatusRuntimeException e) {
            log.warn("StorageService error: {}", e.getStatus());
            throw CarProtoMapper.mapError(e);
        }
    }

    public CarResponse getCarById(UUID id) {
        log.info("Calling StorageService.getCarById id={}", id);
        try {
            CarProto proto = stub.getCarById(
                    GetCarByIdRequest.newBuilder().setId(id.toString()).build()
            );
            return CarProtoMapper.toResponse(proto);
        } catch (StatusRuntimeException e) {
            log.warn("StorageService error: {}", e.getStatus());
            throw CarProtoMapper.mapError(e);
        }
    }
}

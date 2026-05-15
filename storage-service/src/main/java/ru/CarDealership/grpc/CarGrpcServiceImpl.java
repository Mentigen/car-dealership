package ru.CarDealership.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.CarDealership.domain.car.Car;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.service.CarService;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class CarGrpcServiceImpl extends CarGrpcServiceGrpc.CarGrpcServiceImplBase {

    private final CarService carService;

    @Override
    public void getAvailableCars(GetAvailableCarsRequest request,
                                 StreamObserver<GetAvailableCarsResponse> responseObserver) {
        List<Car> cars = carService.findAllCars();
        GetAvailableCarsResponse response = GetAvailableCarsResponse.newBuilder()
                .addAllCars(cars.stream().map(this::toProto).toList())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCarById(GetCarByIdRequest request,
                           StreamObserver<CarProto> responseObserver) {
        try {
            Car car = carService.findCarById(UUID.fromString(request.getId()));
            responseObserver.onNext(toProto(car));
            responseObserver.onCompleted();
        } catch (EntityNotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException()
            );
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription("Invalid car ID format").asRuntimeException()
            );
        }
    }

    private CarProto toProto(Car car) {
        var model = car.getCarConfiguration().getModel();
        return CarProto.newBuilder()
                .setId(car.getId().toString())
                .setBrand(model.getBrand())
                .setModelName(model.getModelName())
                .setPrice(car.getPrice().doubleValue())
                .setBodyType(model.getBody().name())
                .setFuelType(model.getFuel().name())
                .setDriveType(model.getDrive().name())
                .setEnginePower(model.getEnginePower().getValue())
                .setEngineVolume(model.getEngineVolume().getValue())
                .build();
    }
}

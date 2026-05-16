package ru.CarDealership;

import io.grpc.*;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.exceptions.ServiceUnavailableException;
import ru.CarDealership.grpc.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarGrpcClientIT extends BaseIntegrationTest {

    private static final String SERVER_NAME = "test-storage-client";
    private static Server fakeServer;

    private static final UUID CAR_ID = UUID.randomUUID();

    private static final CarProto SAMPLE_CAR = CarProto.newBuilder()
            .setId(CAR_ID.toString())
            .setBrand("Toyota")
            .setModelName("Camry")
            .setPrice(2500000.0)
            .setBodyType("SEDAN")
            .setFuelType("PETROL")
            .setDriveType("FWD")
            .setEnginePower(150)
            .setEngineVolume(2.5)
            .build();

    @DynamicPropertySource
    static void grpcClientProperties(DynamicPropertyRegistry registry) {
        registry.add("grpc.client.storage-service.address", () -> "in-process:" + SERVER_NAME);
        registry.add("grpc.client.storage-service.negotiation-type", () -> "PLAINTEXT");
    }

    @BeforeAll
    static void startFakeServer() throws IOException {
        fakeServer = InProcessServerBuilder.forName(SERVER_NAME)
                .directExecutor()
                .addService(new FakeCarService())
                .build()
                .start();
    }

    @AfterAll
    static void stopFakeServer() throws InterruptedException {
        if (fakeServer != null) {
            fakeServer.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Autowired
    private GrpcCarClient grpcCarClient;

    @Test
    void getAvailableCars_returnsCarList() {
        List<CarResponse> cars = grpcCarClient.getAvailableCars();
        assertThat(cars).hasSize(1);
        assertThat(cars.get(0).brand()).isEqualTo("Toyota");
        assertThat(cars.get(0).modelName()).isEqualTo("Camry");
        assertThat(cars.get(0).enginePower()).isEqualTo(150);
    }

    @Test
    void getCarById_existingId_returnsCar() {
        CarResponse car = grpcCarClient.getCarById(CAR_ID);
        assertThat(car.brand()).isEqualTo("Toyota");
        assertThat(car.engineVolume()).isEqualTo(2.5);
    }

    @Test
    void getCarById_unknownId_throwsEntityNotFoundException() {
        assertThatThrownBy(() -> grpcCarClient.getCarById(UUID.randomUUID()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getAvailableCars_serverUnavailable_throwsServiceUnavailableException() throws Exception {
        Server unavailableServer = InProcessServerBuilder.forName("unavailable-server")
                .directExecutor()
                .addService(new UnavailableCarService())
                .build()
                .start();
        try {
            ManagedChannel channel = InProcessChannelBuilder.forName("unavailable-server")
                    .directExecutor()
                    .build();
            try {
                GrpcCarClient testClient = new GrpcCarClient();
                ReflectionTestUtils.setField(testClient, "stub",
                        CarGrpcServiceGrpc.newBlockingStub(channel));

                assertThatThrownBy(testClient::getAvailableCars)
                        .isInstanceOf(ServiceUnavailableException.class);
            } finally {
                channel.shutdown().awaitTermination(2, TimeUnit.SECONDS);
            }
        } finally {
            unavailableServer.shutdown().awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    @Test
    void getAvailableCars_deadlineExceeded_throwsServiceUnavailableException() throws Exception {
        Server slowServer = InProcessServerBuilder.forName("slow-server")
                .directExecutor()
                .addService(new SlowCarService())
                .build()
                .start();
        try {
            ManagedChannel channel = InProcessChannelBuilder.forName("slow-server")
                    .directExecutor()
                    .build();
            try {
                GrpcCarClient testClient = new GrpcCarClient();
                ReflectionTestUtils.setField(testClient, "stub",
                        CarGrpcServiceGrpc.newBlockingStub(channel)
                                .withDeadlineAfter(100, TimeUnit.MILLISECONDS));

                assertThatThrownBy(testClient::getAvailableCars)
                        .isInstanceOf(ServiceUnavailableException.class);
            } finally {
                channel.shutdown().awaitTermination(2, TimeUnit.SECONDS);
            }
        } finally {
            slowServer.shutdownNow().awaitTermination(3, TimeUnit.SECONDS);
        }
    }

    static class FakeCarService extends CarGrpcServiceGrpc.CarGrpcServiceImplBase {

        @Override
        public void getAvailableCars(GetAvailableCarsRequest request,
                                     StreamObserver<GetAvailableCarsResponse> responseObserver) {
            responseObserver.onNext(GetAvailableCarsResponse.newBuilder()
                    .addCars(SAMPLE_CAR)
                    .build());
            responseObserver.onCompleted();
        }

        @Override
        public void getCarById(GetCarByIdRequest request,
                               StreamObserver<CarProto> responseObserver) {
            if (request.getId().equals(CAR_ID.toString())) {
                responseObserver.onNext(SAMPLE_CAR);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(
                        Status.NOT_FOUND.withDescription("Car not found").asRuntimeException()
                );
            }
        }
    }

    static class UnavailableCarService extends CarGrpcServiceGrpc.CarGrpcServiceImplBase {
        @Override
        public void getAvailableCars(GetAvailableCarsRequest request,
                                     StreamObserver<GetAvailableCarsResponse> responseObserver) {
            responseObserver.onError(Status.UNAVAILABLE.asRuntimeException());
        }
    }

    static class SlowCarService extends CarGrpcServiceGrpc.CarGrpcServiceImplBase {
        @Override
        public void getAvailableCars(GetAvailableCarsRequest request,
                                     StreamObserver<GetAvailableCarsResponse> responseObserver) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                responseObserver.onError(Status.CANCELLED.asRuntimeException());
                return;
            }
            responseObserver.onNext(GetAvailableCarsResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }
}

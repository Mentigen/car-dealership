package ru.CarDealership;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.CarDealership.grpc.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static io.grpc.Status.Code.NOT_FOUND;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "grpc.server.in-process-name=storage-test",
                "grpc.server.port=-1"
        }
)
class CarGrpcServerIT extends BaseIntegrationTest {

    private ManagedChannel channel;
    private CarGrpcServiceGrpc.CarGrpcServiceBlockingStub stub;

    @BeforeEach
    void setupChannel() {
        channel = InProcessChannelBuilder.forName("storage-test")
                .directExecutor()
                .build();
        stub = CarGrpcServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void getAvailableCars_returnsSeedCars() {
        GetAvailableCarsResponse response = stub.getAvailableCars(
                GetAvailableCarsRequest.newBuilder().build()
        );
        assertThat(response.getCarsList()).isNotEmpty();
        assertThat(response.getCarsList().get(0).getId()).isNotEmpty();
        assertThat(response.getCarsList().get(0).getBrand()).isNotEmpty();
    }

    @Test
    void getCarById_unknownId_throwsStatusNotFound() {
        assertThatThrownBy(() ->
                stub.getCarById(GetCarByIdRequest.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .build())
        )
                .isInstanceOf(StatusRuntimeException.class)
                .satisfies(e -> assertThat(
                        ((StatusRuntimeException) e).getStatus().getCode()
                ).isEqualTo(NOT_FOUND));
    }
}

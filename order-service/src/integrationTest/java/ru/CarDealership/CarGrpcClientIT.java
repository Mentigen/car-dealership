package ru.CarDealership;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.grpc.GrpcCarClient;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CarGrpcClientIT extends BaseIntegrationTest {

    static final Network storageNetwork = Network.newNetwork();

    @Container
    static final PostgreSQLContainer<?> storagePostgres = new PostgreSQLContainer<>("postgres:15")
            .withNetwork(storageNetwork)
            .withNetworkAliases("storage-postgres")
            .withDatabaseName("storageservice")
            .withUsername("cardealership")
            .withPassword("cardealership");

    @Container
    static final RabbitMQContainer storageRabbit = new RabbitMQContainer("rabbitmq:3-management")
            .withNetwork(storageNetwork)
            .withNetworkAliases("storage-rabbit");

    @Container
    static final GenericContainer<?> storageService = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withDockerfile(Paths.get("../storage-service/docker/Dockerfile"))
                    .withFileFromPath(".", Paths.get("../storage-service"))
    )
            .withNetwork(storageNetwork)
            .withExposedPorts(9091, 8081)
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://storage-postgres:5432/storageservice")
            .withEnv("SPRING_DATASOURCE_USERNAME", "cardealership")
            .withEnv("SPRING_DATASOURCE_PASSWORD", "cardealership")
            .withEnv("SPRING_RABBITMQ_HOST", "storage-rabbit")
            .withEnv("SPRING_RABBITMQ_PORT", "5672")
            .withEnv("SPRING_RABBITMQ_USERNAME", "guest")
            .withEnv("SPRING_RABBITMQ_PASSWORD", "guest")
            .withEnv("SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI",
                    "http://localhost:9999/realms/cardealership/protocol/openid-connect/certs")
            .dependsOn(storageRabbit, storagePostgres)
            .waitingFor(Wait.forHttp("/actuator/health")
                    .forPort(8081)
                    .withStartupTimeout(Duration.ofMinutes(2)));

    private static final UUID CAR_MODEL_ID = UUID.fromString("b0000000-0000-0000-0000-000000000001");

    @DynamicPropertySource
    static void grpcClientProperties(DynamicPropertyRegistry registry) {
        registry.add("grpc.client.storage-service.address",
                () -> "static://localhost:" + storageService.getMappedPort(9091));
        registry.add("grpc.client.storage-service.negotiation-type", () -> "PLAINTEXT");
    }

    @Autowired
    private GrpcCarClient grpcCarClient;

    @Test
    void getAvailableCars_emptyDatabase_returnEmptyList() {
        assertThat(grpcCarClient.getAvailableCars()).isEmpty();
    }

    @Test
    void getAvailableCars_withCar_returnsCarList() throws Exception {
        UUID configId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        seedCar(configId, carId);
        try {
            List<CarResponse> cars = grpcCarClient.getAvailableCars();
            assertThat(cars).hasSize(1);
            assertThat(cars.get(0).brand()).isEqualTo("Toyota");
            assertThat(cars.get(0).modelName()).isEqualTo("Camry");
        } finally {
            cleanupCar(configId, carId);
        }
    }

    private void seedCar(UUID configId, UUID carId) throws Exception {
        try (Connection conn = DriverManager.getConnection(
                storagePostgres.getJdbcUrl(),
                storagePostgres.getUsername(),
                storagePostgres.getPassword())) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO car_configurations (id, created_at, updated_at, removed, car_model_id) " +
                    "VALUES (?, NOW(), NOW(), FALSE, ?)")) {
                ps.setObject(1, configId);
                ps.setObject(2, CAR_MODEL_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO cars (id, created_at, updated_at, removed, car_configuration_id, available_for_test_drive) " +
                    "VALUES (?, NOW(), NOW(), FALSE, ?, FALSE)")) {
                ps.setObject(1, carId);
                ps.setObject(2, configId);
                ps.executeUpdate();
            }
        }
    }

    private void cleanupCar(UUID configId, UUID carId) throws Exception {
        try (Connection conn = DriverManager.getConnection(
                storagePostgres.getJdbcUrl(),
                storagePostgres.getUsername(),
                storagePostgres.getPassword())) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cars WHERE id = ?")) {
                ps.setObject(1, carId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM car_configurations WHERE id = ?")) {
                ps.setObject(1, configId);
                ps.executeUpdate();
            }
        }
    }

}
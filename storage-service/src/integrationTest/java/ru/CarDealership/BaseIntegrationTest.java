package ru.CarDealership;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.CarDealership.messaging.AssemblyOrderEventProducer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"grpc.server.port=0"})
@AutoConfigureMockMvc
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("storageservice")
            .withUsername("cardealership")
            .withPassword("cardealership");

    @Container
    static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:3-management");

    @MockBean
    JwtDecoder jwtDecoder;

    @MockBean
    AssemblyOrderEventProducer eventProducer;

    @BeforeEach
    void setupJwtDecoder() {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(3600);

        Mockito.lenient().when(jwtDecoder.decode("manager-token")).thenReturn(
                Jwt.withTokenValue("manager-token")
                        .header("alg", "none")
                        .claim("sub", "a0000000-0000-0000-0000-000000000001")
                        .claim("email", "manager@dealer.ru")
                        .claim("preferred_username", "manager")
                        .claim("realm_access", Map.of("roles", List.of("MANAGER")))
                        .issuedAt(now)
                        .expiresAt(exp)
                        .build()
        );

        Mockito.lenient().when(jwtDecoder.decode("customer-token")).thenReturn(
                Jwt.withTokenValue("customer-token")
                        .header("alg", "none")
                        .claim("sub", "a0000000-0000-0000-0000-000000000002")
                        .claim("email", "customer@mail.ru")
                        .claim("preferred_username", "customer")
                        .claim("realm_access", Map.of("roles", List.of("USER")))
                        .issuedAt(now)
                        .expiresAt(exp)
                        .build()
        );

        Mockito.lenient().when(jwtDecoder.decode("admin-token")).thenReturn(
                Jwt.withTokenValue("admin-token")
                        .header("alg", "none")
                        .claim("sub", "a0000000-0000-0000-0000-000000000003")
                        .claim("email", "admin@dealer.ru")
                        .claim("preferred_username", "admin")
                        .claim("realm_access", Map.of("roles", List.of("ADMIN")))
                        .issuedAt(now)
                        .expiresAt(exp)
                        .build()
        );

        Mockito.lenient().when(jwtDecoder.decode("warehouse-token")).thenReturn(
                Jwt.withTokenValue("warehouse-token")
                        .header("alg", "none")
                        .claim("sub", "a0000000-0000-0000-0000-000000000004")
                        .claim("email", "warehouse@dealer.ru")
                        .claim("preferred_username", "warehouse")
                        .claim("realm_access", Map.of("roles", List.of("WAREHOUSE_ADMIN")))
                        .issuedAt(now)
                        .expiresAt(exp)
                        .build()
        );
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.rabbitmq.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.port", rabbitmq::getAmqpPort);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> "http://localhost:8180/realms/cardealership");
    }

    protected HttpHeaders authHeaders(String tokenType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenType);
        return headers;
    }

    protected <T> HttpEntity<T> authRequest(T body, String tokenType) {
        return new HttpEntity<>(body, authHeaders(tokenType));
    }

    protected HttpEntity<Void> authRequest(String tokenType) {
        return new HttpEntity<>(authHeaders(tokenType));
    }
}

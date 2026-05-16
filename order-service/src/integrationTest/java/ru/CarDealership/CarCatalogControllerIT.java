package ru.CarDealership;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.exceptions.ServiceUnavailableException;
import ru.CarDealership.grpc.GrpcCarClient;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CarCatalogControllerIT extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private GrpcCarClient grpcCarClient;

    private static final UUID CAR_ID = UUID.randomUUID();

    private static final CarResponse SAMPLE_CAR = new CarResponse(
            CAR_ID, "Toyota", "Camry",
            BigDecimal.valueOf(2_500_000), "SEDAN", "PETROL", "FWD", 150, 2.5
    );

    @BeforeEach
    void setupJwt() {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(3600);

        Mockito.lenient().when(jwtDecoder.decode("user-token")).thenReturn(
                Jwt.withTokenValue("user-token")
                        .header("alg", "none")
                        .claim("sub", UUID.randomUUID().toString())
                        .claim("email", "user@test.ru")
                        .claim("preferred_username", "user")
                        .claim("realm_access", Map.of("roles", List.of("USER")))
                        .issuedAt(now).expiresAt(exp).build()
        );
        Mockito.lenient().when(jwtDecoder.decode("manager-token")).thenReturn(
                Jwt.withTokenValue("manager-token")
                        .header("alg", "none")
                        .claim("sub", UUID.randomUUID().toString())
                        .claim("email", "manager@test.ru")
                        .claim("preferred_username", "manager")
                        .claim("realm_access", Map.of("roles", List.of("MANAGER")))
                        .issuedAt(now).expiresAt(exp).build()
        );
    }

    @Test
    void getAvailableCars_asUser_returns200() {
        when(grpcCarClient.getAvailableCars()).thenReturn(List.of(SAMPLE_CAR));

        ResponseEntity<CarResponse[]> response = restTemplate.exchange(
                "/api/v1/cars", HttpMethod.GET,
                authRequest("user-token"),
                CarResponse[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].brand()).isEqualTo("Toyota");
    }

    @Test
    void getCarById_asManager_returns200() {
        when(grpcCarClient.getCarById(CAR_ID)).thenReturn(SAMPLE_CAR);

        ResponseEntity<CarResponse> response = restTemplate.exchange(
                "/api/v1/cars/" + CAR_ID, HttpMethod.GET,
                authRequest("manager-token"),
                CarResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().modelName()).isEqualTo("Camry");
    }

    @Test
    void getAvailableCars_storageUnavailable_returns503() {
        when(grpcCarClient.getAvailableCars())
                .thenThrow(new ServiceUnavailableException("StorageService is unavailable"));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/cars", HttpMethod.GET,
                authRequest("user-token"),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void getCarById_notFound_returns404() {
        when(grpcCarClient.getCarById(any())).thenThrow(new EntityNotFoundException("Car not found"));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/cars/" + UUID.randomUUID(), HttpMethod.GET,
                authRequest("user-token"),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAvailableCars_unauthorized_returns401() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/cars", HttpMethod.GET,
                null,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private org.springframework.http.HttpEntity<Void> authRequest(String token) {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(token);
        return new org.springframework.http.HttpEntity<>(headers);
    }
}

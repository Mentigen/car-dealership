package ru.CarDealership.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class StorageServiceClient {

    private final WebClient webClient;

    public StorageServiceClient(@Value("${storage.service.url}") String storageServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(storageServiceUrl)
                .build();
    }

    public CarInfoResponse getCarById(UUID carId) {
        try {
            return webClient.get()
                    .uri("/api/cars/{id}/info", carId)
                    .retrieve()
                    .bodyToMono(CarInfoResponse.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new EntityNotFoundException("Car not found: " + carId);
        }
    }

    public CarInfoResponse getCarConfigurationById(UUID configId) {
        try {
            return webClient.get()
                    .uri("/api/car-configurations/{id}/info", configId)
                    .retrieve()
                    .bodyToMono(CarInfoResponse.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new EntityNotFoundException("Car configuration not found: " + configId);
        }
    }

    public record CarInfoResponse(UUID id, BigDecimal price) {}
}

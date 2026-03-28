package ru.CarDealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.CarDealership.api.dto.CarModelResponse;

import static org.junit.jupiter.api.Assertions.*;

class CarModelControllerIT extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllCarModels_returnsSeededModels() {
        ResponseEntity<CarModelResponse[]> response =
                restTemplate.getForEntity("/api/car-models", CarModelResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 3);
    }

    @Test
    void getCarModelById_returnsCorrectModel() {
        CarModelResponse[] all = restTemplate.getForObject("/api/car-models", CarModelResponse[].class);
        assertNotNull(all);
        assertTrue(all.length > 0);

        CarModelResponse first = all[0];
        ResponseEntity<CarModelResponse> response =
                restTemplate.getForEntity("/api/car-models/" + first.id(), CarModelResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(first.brand(), response.getBody().brand());
    }
}

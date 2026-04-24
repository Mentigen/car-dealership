package ru.CarDealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.CarDealership.api.dto.AssemblyOrderResponse;
import ru.CarDealership.api.dto.CreateAssemblyOrderRequest;
import ru.CarDealership.api.dto.UpdateAssemblyOrderStatusRequest;
import ru.CarDealership.domain.assembly.AssemblyOrderStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyOrderControllerIT extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndReadAssemblyOrder_asWarehouseAdmin() {
        CreateAssemblyOrderRequest request = new CreateAssemblyOrderRequest();
        request.setSourceOrderId(UUID.randomUUID());
        request.setTraceId(UUID.randomUUID());
        request.setOrderType("STOCK");
        request.setCarId(UUID.randomUUID());

        ResponseEntity<AssemblyOrderResponse> createResponse = restTemplate.exchange(
                "/api/assembly-orders", HttpMethod.POST,
                authRequest(request, "warehouse-token"),
                AssemblyOrderResponse.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals(AssemblyOrderStatus.CREATED, createResponse.getBody().status());
        assertEquals(request.getSourceOrderId(), createResponse.getBody().sourceOrderId());

        ResponseEntity<AssemblyOrderResponse[]> listResponse = restTemplate.exchange(
                "/api/assembly-orders", HttpMethod.GET,
                authRequest("warehouse-token"),
                AssemblyOrderResponse[].class
        );

        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertNotNull(listResponse.getBody());
        assertTrue(listResponse.getBody().length >= 1);

        UUID id = createResponse.getBody().id();
        ResponseEntity<AssemblyOrderResponse> getResponse = restTemplate.exchange(
                "/api/assembly-orders/" + id, HttpMethod.GET,
                authRequest("warehouse-token"),
                AssemblyOrderResponse.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(id, getResponse.getBody().id());
    }

    @Test
    void updateStatus_asWarehouseAdmin() {
        CreateAssemblyOrderRequest request = new CreateAssemblyOrderRequest();
        request.setSourceOrderId(UUID.randomUUID());
        request.setTraceId(UUID.randomUUID());
        request.setOrderType("CUSTOM");
        request.setCarConfigurationId(UUID.randomUUID());

        UUID id = restTemplate.exchange(
                "/api/assembly-orders", HttpMethod.POST,
                authRequest(request, "warehouse-token"),
                AssemblyOrderResponse.class
        ).getBody().id();

        UpdateAssemblyOrderStatusRequest updateRequest = new UpdateAssemblyOrderStatusRequest();
        updateRequest.setStatus(AssemblyOrderStatus.FAIL);
        updateRequest.setFailReason("No parts available");

        ResponseEntity<AssemblyOrderResponse> updateResponse = restTemplate.exchange(
                "/api/assembly-orders/" + id + "/status", HttpMethod.PUT,
                authRequest(updateRequest, "warehouse-token"),
                AssemblyOrderResponse.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(AssemblyOrderStatus.FAIL, updateResponse.getBody().status());
        assertEquals("No parts available", updateResponse.getBody().failReason());
    }

    @Test
    void delete_asAdmin() {
        CreateAssemblyOrderRequest request = new CreateAssemblyOrderRequest();
        request.setSourceOrderId(UUID.randomUUID());
        request.setTraceId(UUID.randomUUID());
        request.setOrderType("STOCK");
        request.setCarId(UUID.randomUUID());

        UUID id = restTemplate.exchange(
                "/api/assembly-orders", HttpMethod.POST,
                authRequest(request, "warehouse-token"),
                AssemblyOrderResponse.class
        ).getBody().id();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/assembly-orders/" + id, HttpMethod.DELETE,
                authRequest("admin-token"),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<Void> secondDelete = restTemplate.exchange(
                "/api/assembly-orders/" + id, HttpMethod.DELETE,
                authRequest("warehouse-token"),
                Void.class
        );

        assertEquals(HttpStatus.FORBIDDEN, secondDelete.getStatusCode());
    }

    @Test
    void assemblyOrderEndpoints_withoutToken_return401() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/assembly-orders", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void assemblyOrderEndpoints_withManagerRole_return403() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/assembly-orders", HttpMethod.GET,
                authRequest("manager-token"),
                String.class
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void delete_withWarehouseRole_returns403() {
        CreateAssemblyOrderRequest request = new CreateAssemblyOrderRequest();
        request.setSourceOrderId(UUID.randomUUID());
        request.setTraceId(UUID.randomUUID());
        request.setOrderType("STOCK");
        request.setCarId(UUID.randomUUID());

        UUID id = restTemplate.exchange(
                "/api/assembly-orders", HttpMethod.POST,
                authRequest(request, "warehouse-token"),
                AssemblyOrderResponse.class
        ).getBody().id();

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/assembly-orders/" + id, HttpMethod.DELETE,
                authRequest("warehouse-token"),
                String.class
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}

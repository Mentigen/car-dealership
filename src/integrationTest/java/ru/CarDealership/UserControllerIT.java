package ru.CarDealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.CarDealership.api.dto.UserRegisterRequest;
import ru.CarDealership.api.dto.UserResponse;
import ru.CarDealership.domain.user.Role;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerIT extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllUsers_returnsSeededUsers() {
        ResponseEntity<UserResponse[]> response = restTemplate.exchange(
                "/api/users", HttpMethod.GET,
                authRequest("manager-token"),
                UserResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 4);
    }

    @Test
    void getAllUsers_withoutToken_returns401() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getAllUsers_withUserRole_returns403() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/users", HttpMethod.GET,
                authRequest("customer-token"),
                String.class
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getUsersByRole_filtersCorrectly() {
        ResponseEntity<UserResponse[]> response = restTemplate.exchange(
                "/api/users/by-role?role=MANAGER", HttpMethod.GET,
                authRequest("manager-token"),
                UserResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (UserResponse user : response.getBody()) {
            assertEquals(Role.MANAGER, user.role());
        }
    }

    @Test
    void registerUser_createsNewUser() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("New");
        request.setLastName("User");
        request.setEmail("new_" + UUID.randomUUID() + "@test.ru");
        request.setPassword("password123");
        request.setRole(Role.USER);

        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/api/users", HttpMethod.POST,
                authRequest(request, "admin-token"),
                UserResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New", response.getBody().firstName());
    }

    @Test
    void registerUser_withManagerRole_returns403() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmail("blocked_" + UUID.randomUUID() + "@test.ru");
        request.setPassword("password123");
        request.setRole(Role.USER);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/users", HttpMethod.POST,
                authRequest(request, "manager-token"),
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}

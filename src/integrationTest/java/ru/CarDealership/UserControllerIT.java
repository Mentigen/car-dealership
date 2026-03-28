package ru.CarDealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
        ResponseEntity<UserResponse[]> response =
                restTemplate.getForEntity("/api/users", UserResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 4);
    }

    @Test
    void getUsersByRole_filtersCorrectly() {
        ResponseEntity<UserResponse[]> response =
                restTemplate.getForEntity("/api/users/by-role?role=MANAGER", UserResponse[].class);

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
        request.setRole(Role.CUSTOMER);

        ResponseEntity<UserResponse> response =
                restTemplate.postForEntity("/api/users", request, UserResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New", response.getBody().firstName());
    }
}

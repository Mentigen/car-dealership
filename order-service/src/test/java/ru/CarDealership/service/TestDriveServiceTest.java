package ru.CarDealership.service;

import ru.CarDealership.domain.order.TestDriveRequest;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.infrastructure.repository.InMemoryTestDriveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TestDriveServiceTest {

    private TestDriveService testDriveService;
    private InMemoryTestDriveRequestRepository requestRepository;
    private User client;
    private UUID carId;

    @BeforeEach
    void setUp() {
        requestRepository = new InMemoryTestDriveRequestRepository();
        testDriveService = new TestDriveService(requestRepository);

        client = new User(UUID.randomUUID(), "Ivan", "Ivanov", Role.USER, "ivan@ya.ru", "123", "pass");
        carId = UUID.randomUUID();
    }

    @Test
    void testRequestTestDrive() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, carId, time);

        assertNotNull(request);
        assertNotNull(request.getId());
        assertEquals("PENDING", request.getState().getName());
        assertEquals(client, request.getClient());
        assertEquals(carId, request.getCarId());
    }

    @Test
    void testApproveRequest() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, carId, time);
        testDriveService.approveRequest(request.getId());

        TestDriveRequest updated = requestRepository.findById(request.getId()).orElseThrow();
        assertEquals("APPROVED", updated.getState().getName());
    }

    @Test
    void testRejectRequest() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, carId, time);
        testDriveService.rejectRequest(request.getId());

        TestDriveRequest updated = requestRepository.findById(request.getId()).orElseThrow();
        assertEquals("CANCELLED", updated.getState().getName());
    }

    @Test
    void testCompleteRequest() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, carId, time);
        testDriveService.approveRequest(request.getId());
        testDriveService.completeRequest(request.getId());

        TestDriveRequest updated = requestRepository.findById(request.getId()).orElseThrow();
        assertEquals("DONE", updated.getState().getName());
    }

    @Test
    void testInvalidTransitions() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, carId, time);

        assertThrows(IllegalStateException.class, () -> testDriveService.completeRequest(request.getId()));

        testDriveService.approveRequest(request.getId());
        assertThrows(IllegalStateException.class, () -> testDriveService.approveRequest(request.getId()));
    }
}

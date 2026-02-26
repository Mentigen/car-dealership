package org.example.service;

import org.example.domain.car.*;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.domain.order.TestDriveRequest;
import org.example.domain.order.TestDriveStatus;
import org.example.domain.user.Role;
import org.example.domain.user.User;
import org.example.infrastructure.repository.InMemoryCarRepository;
import org.example.infrastructure.repository.InMemoryTestDriveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TestDriveServiceTest {

    private TestDriveService testDriveService;
    private InMemoryCarRepository carRepository;
    private InMemoryTestDriveRequestRepository requestRepository;
    private User client;
    private Car car;

    @BeforeEach
    void setUp() {
        carRepository = new InMemoryCarRepository();
        requestRepository = new InMemoryTestDriveRequestRepository();
        testDriveService = new TestDriveService(carRepository, requestRepository);

        client = new User("Ivan", "Ivanov", Role.CUSTOMER, "ivan@ya.ru", "123", "pass");
        
        CarModel model = new CarModel();
        model.setId(UUID.randomUUID());
        model.setBrand("TestBrand");
        model.setPrice(new BigDecimal("1000000"));

        
        List<UUID> compatibleIds = List.of(model.getId());
        Part wheel = new Part(UUID.randomUUID(), PartType.WHEEL, BigDecimal.ZERO, compatibleIds);
        TransmissionPart trans = new TransmissionPart(UUID.randomUUID(), PartType.TRANSMISSION, BigDecimal.ZERO, compatibleIds, TransmissionType.AUTOMATIC);
        Part steer = new Part(UUID.randomUUID(), PartType.STEERING_WHEEL, BigDecimal.ZERO, compatibleIds);
        Part interior = new Part(UUID.randomUUID(), PartType.INTERIOR, BigDecimal.ZERO, compatibleIds);
        ColorPart color = new ColorPart(UUID.randomUUID(), PartType.COLOR, BigDecimal.ZERO, compatibleIds, "Red");
        
        CarConfiguration config = new CarConfiguration.Builder(model)
                .addPart(wheel)
                .addPart(trans)
                .addPart(steer)
                .addPart(interior)
                .addPart(color)
                .build();
        
        car = new Car(UUID.randomUUID(), config);
        carRepository.save(car);
    }

    @Test
    void testRequestTestDrive() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, car, time);

        assertNotNull(request);
        assertNotNull(request.getId());
        assertEquals(TestDriveStatus.PENDING, request.getStatus());
        assertEquals(client, request.getClient());
        assertEquals(car, request.getCar());
    }

    @Test
    void testApproveRequest() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, car, time);

        testDriveService.approveRequest(request.getId());

        TestDriveRequest updated = requestRepository.findById(request.getId()).orElseThrow();
        assertEquals(TestDriveStatus.APPROVED, updated.getStatus());
    }

    @Test
    void testRejectRequest() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, car, time);

        testDriveService.rejectRequest(request.getId());

        TestDriveRequest updated = requestRepository.findById(request.getId()).orElseThrow();
        assertEquals(TestDriveStatus.CANCELLED, updated.getStatus());
    }

    @Test
    void testCompleteRequest() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, car, time);
        testDriveService.approveRequest(request.getId());

        testDriveService.completeRequest(request.getId());

        TestDriveRequest updated = requestRepository.findById(request.getId()).orElseThrow();
        assertEquals(TestDriveStatus.DONE, updated.getStatus());
    }
    
    @Test
    void testInvalidTransitions() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        TestDriveRequest request = testDriveService.requestTestDrive(client, car, time);
        
        assertThrows(IllegalStateException.class, () -> testDriveService.completeRequest(request.getId()));
        
        testDriveService.approveRequest(request.getId());
        assertThrows(IllegalStateException.class, () -> testDriveService.approveRequest(request.getId()));
    }
    
    @Test
    void testManageTestDriveCars() {
        testDriveService.addCarToTestDrive(car);
        List<Car> available = testDriveService.getTestDriveCars();
        assertEquals(1, available.size());
        assertEquals(car.getId(), available.get(0).getId());
        
        testDriveService.removeCarFromTestDrive(car);
        available = testDriveService.getTestDriveCars();
        assertTrue(available.isEmpty());
    }
}

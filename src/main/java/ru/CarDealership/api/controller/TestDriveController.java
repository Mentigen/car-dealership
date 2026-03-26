package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.CreateTestDriveRequest;
import ru.CarDealership.api.dto.TestDriveRequestResponse;
import ru.CarDealership.api.mapper.TestDriveRequestDtoMapper;
import ru.CarDealership.service.CarService;
import ru.CarDealership.service.TestDriveService;
import ru.CarDealership.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/test-drives")
@AllArgsConstructor
@Tag(name = "Test Drives", description = "Test drive request management")
public class TestDriveController {

    private final TestDriveService testDriveService;
    private final UserService userService;
    private final CarService carService;
    private final TestDriveRequestDtoMapper mapper;

    @GetMapping
    @Operation(summary = "Get all test drive requests")
    public List<TestDriveRequestResponse> findAll() {
        return testDriveService.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Request a test drive")
    public TestDriveRequestResponse create(@Valid @RequestBody CreateTestDriveRequest request) {
        return mapper.toResponse(testDriveService.requestTestDrive(
                userService.findById(request.getClientId()),
                carService.findCarById(request.getCarId()),
                request.getStartTime()
        ));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve test drive request")
    public void approve(@PathVariable UUID id) {
        testDriveService.approveRequest(id);
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject test drive request")
    public void reject(@PathVariable UUID id) {
        testDriveService.rejectRequest(id);
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Mark test drive as completed")
    public void complete(@PathVariable UUID id) {
        testDriveService.completeRequest(id);
    }
}

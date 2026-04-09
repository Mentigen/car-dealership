package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.CreateTestDriveRequest;
import ru.CarDealership.api.dto.TestDriveRequestResponse;
import ru.CarDealership.api.mapper.TestDriveRequestDtoMapper;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.security.AuthenticatedUserResolver;
import ru.CarDealership.service.CarService;
import ru.CarDealership.service.TestDriveService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/test-drives")
@AllArgsConstructor
@Tag(name = "Test Drives", description = "Test drive request management")
public class TestDriveController {

    private final TestDriveService testDriveService;
    private final CarService carService;
    private final TestDriveRequestDtoMapper mapper;
    private final AuthenticatedUserResolver userResolver;

    @GetMapping
    @Operation(summary = "Get all test drive requests")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<TestDriveRequestResponse> findAll() {
        return testDriveService.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Request a test drive")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public TestDriveRequestResponse create(
            @Valid @RequestBody CreateTestDriveRequest request,
            Authentication authentication) {
        User client = userResolver.resolve(authentication);
        return mapper.toResponse(testDriveService.requestTestDrive(
                client,
                carService.findCarById(request.getCarId()),
                request.getStartTime()
        ));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve test drive request")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void approve(@PathVariable UUID id) {
        testDriveService.approveRequest(id);
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject test drive request")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void reject(@PathVariable UUID id) {
        testDriveService.rejectRequest(id);
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Mark test drive as completed")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void complete(@PathVariable UUID id) {
        testDriveService.completeRequest(id);
    }
}

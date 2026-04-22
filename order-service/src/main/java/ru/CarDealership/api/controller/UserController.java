package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.UserRegisterRequest;
import ru.CarDealership.api.dto.UserResponse;
import ru.CarDealership.api.mapper.UserDtoMapper;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name = "Users", description = "User management")
public class UserController {

    private final UserService userService;
    private final UserDtoMapper mapper;

    @GetMapping
    @Operation(summary = "Get all users")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<UserResponse> findAll() {
        return userService.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/by-role")
    @Operation(summary = "Get users by role")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<UserResponse> findByRole(@RequestParam Role role) {
        return userService.findByRole(role).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/by-email")
    @Operation(summary = "Get user by email")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public UserResponse findByEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse register(@Valid @RequestBody UserRegisterRequest request) {
        return mapper.toResponse(userService.register(
                request.getEmail(), request.getPassword(),
                request.getFirstName(), request.getLastName(),
                request.getPhone(), request.getRole()));
    }
}

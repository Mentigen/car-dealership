package ru.CarDealership.api.dto;

import ru.CarDealership.domain.user.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        Role role,
        String email,
        String phone
) {}

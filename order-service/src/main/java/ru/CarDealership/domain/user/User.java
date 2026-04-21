package ru.CarDealership.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class User {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final Role role;
    private final String email;
    private final String phone;
    private final String passwordHash;
}

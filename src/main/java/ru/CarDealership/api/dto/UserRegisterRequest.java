package ru.CarDealership.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.CarDealership.domain.user.Role;

@Data
public class UserRegisterRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String phone;
    @NotNull
    private Role role;
}

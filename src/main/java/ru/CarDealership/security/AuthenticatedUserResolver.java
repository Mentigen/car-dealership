package ru.CarDealership.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;

@Component
@AllArgsConstructor
public class AuthenticatedUserResolver {

    private final UserRepository userRepository;

    public User resolve(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        return userRepository.findByEmail(email).stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found: " + email));
    }
}

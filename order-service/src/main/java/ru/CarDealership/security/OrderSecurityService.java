package ru.CarDealership.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import ru.CarDealership.domain.order.OrderRepository;

import java.util.UUID;

@Component("orderSecurity")
@AllArgsConstructor
public class OrderSecurityService {

    private final OrderRepository orderRepository;

    public boolean isOwner(UUID orderId, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        if (email == null) return false;
        return orderRepository.findById(orderId)
                .map(order -> email.equals(order.getClient().getEmail()))
                .orElse(false);
    }
}

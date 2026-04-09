package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.*;
import ru.CarDealership.api.mapper.OrderDtoMapper;
import ru.CarDealership.domain.car.Car;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.security.AuthenticatedUserResolver;
import ru.CarDealership.service.CarService;
import ru.CarDealership.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
@Tag(name = "Orders", description = "Order management")
public class OrderController {

    private final OrderService orderService;
    private final CarService carService;
    private final OrderDtoMapper mapper;
    private final AuthenticatedUserResolver userResolver;

    @GetMapping
    @Operation(summary = "Get orders (own for USER, all for MANAGER/ADMIN)")
    public List<OrderResponse> findAll(Authentication authentication) {
        User user = userResolver.resolve(authentication);
        boolean privileged = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")
                        || a.getAuthority().equals("ROLE_ADMIN"));
        return orderService.findOrdersVisibleTo(user, privileged).stream()
                .map(mapper::toPolymorphicResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') or (hasRole('USER') and @orderSecurity.isOwner(#id, authentication))")
    public OrderResponse findById(@PathVariable UUID id) {
        return orderService.findOrderById(id)
                .map(mapper::toPolymorphicResponse)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @PostMapping("/stock")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create stock order")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public StockOrderResponse createStockOrder(
            @Valid @RequestBody CreateStockOrderRequest request,
            Authentication authentication) {
        User client = userResolver.resolve(authentication);
        Car car = carService.findCarById(request.getCarId());
        return mapper.toStockResponse(orderService.createStockOrder(client, car));
    }

    @PostMapping("/custom")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create custom order (CarConfiguration must already exist)")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CustomOrderResponse createCustomOrder(
            @Valid @RequestBody CreateCustomOrderRequest request,
            Authentication authentication) {
        User client = userResolver.resolve(authentication);
        return mapper.toCustomResponse(orderService.createCustomOrder(
                client,
                carService.findConfigurationById(request.getCarConfigurationId())
        ));
    }

    @PutMapping("/{id}/next")
    @Operation(summary = "Advance order to next state")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public OrderResponse nextStep(@PathVariable UUID id) {
        return mapper.toPolymorphicResponse(orderService.nextStep(id));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel order")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @orderSecurity.isOwner(#id, authentication))")
    public OrderResponse cancelOrder(@PathVariable UUID id) {
        return mapper.toPolymorphicResponse(orderService.cancelOrder(id));
    }
}

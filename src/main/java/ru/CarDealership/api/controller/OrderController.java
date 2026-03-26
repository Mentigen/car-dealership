package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.*;
import ru.CarDealership.api.mapper.OrderDtoMapper;
import ru.CarDealership.domain.car.Car;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.service.CarService;
import ru.CarDealership.service.OrderService;
import ru.CarDealership.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
@Tag(name = "Orders", description = "Order management")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CarService carService;
    private final OrderDtoMapper mapper;

    @GetMapping
    @Operation(summary = "Get all orders")
    public List<OrderResponse> findAll() {
        return orderService.findOrders().stream()
                .map(mapper::toPolymorphicResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id")
    public OrderResponse findById(@PathVariable UUID id) {
        return orderService.findOrderById(id)
                .map(mapper::toPolymorphicResponse)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @PostMapping("/stock")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create stock order")
    public StockOrderResponse createStockOrder(@Valid @RequestBody CreateStockOrderRequest request) {
        return mapper.toStockResponse(orderService.createStockOrder(
                userService.findById(request.getClientId()),
                carService.findCarById(request.getCarId())
        ));
    }

    @PostMapping("/custom")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create custom order (CarConfiguration must already exist)")
    public CustomOrderResponse createCustomOrder(@Valid @RequestBody CreateCustomOrderRequest request) {
        return mapper.toCustomResponse(orderService.createCustomOrder(
                userService.findById(request.getClientId()),
                carService.findConfigurationById(request.getCarConfigurationId())
        ));
    }

    @PutMapping("/{id}/next")
    @Operation(summary = "Advance order to next state")
    public OrderResponse nextStep(@PathVariable UUID id) {
        return mapper.toPolymorphicResponse(orderService.nextStep(id));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel order")
    public OrderResponse cancelOrder(@PathVariable UUID id) {
        return mapper.toPolymorphicResponse(orderService.cancelOrder(id));
    }
}

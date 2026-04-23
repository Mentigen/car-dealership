package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.AssemblyOrderResponse;
import ru.CarDealership.api.dto.CreateAssemblyOrderRequest;
import ru.CarDealership.api.dto.UpdateAssemblyOrderStatusRequest;
import ru.CarDealership.api.mapper.AssemblyOrderDtoMapper;
import ru.CarDealership.domain.assembly.AssemblyOrder;
import ru.CarDealership.domain.assembly.AssemblyOrderStatus;
import ru.CarDealership.service.AssemblyOrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assembly-orders")
@AllArgsConstructor
@Tag(name = "Assembly Orders", description = "Assembly order management")
public class AssemblyOrderController {

    private final AssemblyOrderService assemblyOrderService;
    private final AssemblyOrderDtoMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create assembly order")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponse create(@Valid @RequestBody CreateAssemblyOrderRequest request) {
        AssemblyOrder order = AssemblyOrder.builder()
                .sourceOrderId(request.getSourceOrderId())
                .traceId(request.getTraceId())
                .orderType(request.getOrderType())
                .carId(request.getCarId())
                .carConfigurationId(request.getCarConfigurationId())
                .status(AssemblyOrderStatus.CREATED)
                .build();
        return mapper.toResponse(assemblyOrderService.create(order));
    }

    @GetMapping
    @Operation(summary = "Get all assembly orders")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public List<AssemblyOrderResponse> findAll() {
        return assemblyOrderService.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get assembly order by id")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponse findById(@PathVariable UUID id) {
        return mapper.toResponse(assemblyOrderService.findById(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update assembly order status")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponse updateStatus(@PathVariable UUID id,
                                               @Valid @RequestBody UpdateAssemblyOrderStatusRequest request) {
        return mapper.toResponse(assemblyOrderService.updateStatus(id, request.getStatus(), request.getFailReason()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete assembly order")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        assemblyOrderService.delete(id);
    }
}

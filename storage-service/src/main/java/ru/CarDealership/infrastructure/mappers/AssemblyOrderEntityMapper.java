package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.assembly.AssemblyOrder;
import ru.CarDealership.infrastructure.entity.AssemblyOrderEntity;

@Component
public class AssemblyOrderEntityMapper {

    public AssemblyOrder toDomain(AssemblyOrderEntity entity) {
        return AssemblyOrder.builder()
                .id(entity.getId())
                .sourceOrderId(entity.getSourceOrderId())
                .traceId(entity.getTraceId())
                .orderType(entity.getOrderType())
                .carId(entity.getCarId())
                .carConfigurationId(entity.getCarConfigurationId())
                .status(entity.getStatus())
                .failReason(entity.getFailReason())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public AssemblyOrderEntity toEntity(AssemblyOrder order) {
        var entity = new AssemblyOrderEntity(
                order.getSourceOrderId(),
                order.getTraceId(),
                order.getOrderType(),
                order.getCarId(),
                order.getCarConfigurationId(),
                order.getStatus(),
                order.getFailReason()
        );
        entity.setId(order.getId());
        return entity;
    }
}

package ru.CarDealership.api.mapper;

import org.mapstruct.Mapper;
import ru.CarDealership.api.dto.AssemblyOrderResponse;
import ru.CarDealership.domain.assembly.AssemblyOrder;

@Mapper(componentModel = "spring")
public interface AssemblyOrderDtoMapper {
    AssemblyOrderResponse toResponse(AssemblyOrder order);
}

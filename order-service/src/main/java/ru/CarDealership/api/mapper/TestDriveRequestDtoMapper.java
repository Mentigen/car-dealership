package ru.CarDealership.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.CarDealership.api.dto.TestDriveRequestResponse;
import ru.CarDealership.domain.order.TestDriveRequest;

@Mapper(componentModel = "spring")
public interface TestDriveRequestDtoMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(expression = "java(request.getState().getName())", target = "status")
    TestDriveRequestResponse toResponse(TestDriveRequest request);
}

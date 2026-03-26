package ru.CarDealership.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.CarDealership.api.dto.CarModelResponse;
import ru.CarDealership.domain.car.CarModel;

@Mapper(componentModel = "spring")
public interface CarModelDtoMapper {

    @Mapping(source = "price.value", target = "price")
    @Mapping(source = "enginePower.value", target = "enginePower")
    @Mapping(source = "engineVolume.value", target = "engineVolume")
    @Mapping(source = "fuel", target = "fuelType")
    CarModelResponse toResponse(CarModel carModel);
}

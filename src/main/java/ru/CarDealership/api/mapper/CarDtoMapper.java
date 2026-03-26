package ru.CarDealership.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.CarDealership.api.dto.CarConfigurationResponse;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.api.dto.PartResponse;
import ru.CarDealership.domain.car.Car;
import ru.CarDealership.domain.car.CarConfiguration;
import ru.CarDealership.domain.car.Part;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CarModelDtoMapper.class})
public abstract class CarDtoMapper {

    @Autowired
    protected PartDtoMapper partDtoMapper;

    @Mapping(source = "carConfiguration.price", target = "price")
    public abstract CarResponse toResponse(Car car);

    @Mapping(source = "price", target = "totalPrice")
    public abstract CarConfigurationResponse toConfigurationResponse(CarConfiguration configuration);

    protected List<PartResponse> mapParts(List<Part> parts) {
        if (parts == null) return List.of();
        return parts.stream()
                .map(partDtoMapper::toPolymorphicResponse)
                .toList();
    }
}

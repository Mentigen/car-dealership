package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.car.CarConfiguration;
import ru.CarDealership.domain.car.CarModel;
import ru.CarDealership.domain.car.Part;
import ru.CarDealership.infrastructure.entity.CarConfigurationEntity;

import java.util.List;

@Component
public class CarConfigurationEntityMapper {

    private final CarModelEntityMapper carModelMapper;
    private final PartEntityMapper partMapper;

    public CarConfigurationEntityMapper(
        CarModelEntityMapper carModelMapper,
        PartEntityMapper partMapper
    ) {
        this.carModelMapper = carModelMapper;
        this.partMapper = partMapper;
    }

    public CarConfiguration toDomain(CarConfigurationEntity entity) {
        CarModel model = carModelMapper.toDomain(entity.getCarModel());
        List<Part> parts = entity.getParts().stream()
            .map(part -> partMapper.toDomain(part))
            .toList();

        return CarConfiguration.reconstruct(entity.getId(), model, parts);
    }
}

package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.car.Car;
import ru.CarDealership.infrastructure.entity.CarEntity;

@Component
public class CarEntityMapper {

    private final CarConfigurationEntityMapper configMapper;

    public CarEntityMapper(CarConfigurationEntityMapper configMapper) {
        this.configMapper = configMapper;
    }

    public Car toDomain(CarEntity entity) {
        return new Car(
            entity.getId(),
            configMapper.toDomain(entity.getCarConfiguration())
        );
    }
}

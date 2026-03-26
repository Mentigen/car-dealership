package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.car.CarModel;
import ru.CarDealership.domain.car.EnginePower;
import ru.CarDealership.domain.car.EngineVolume;
import ru.CarDealership.domain.car.Price;
import ru.CarDealership.infrastructure.entity.CarModelEntity;

@Component
public class CarModelEntityMapper {
    public CarModel toDomain(CarModelEntity entity) {
        return new CarModel(
                entity.getId(),
                entity.getBrand(),
                entity.getModelName(),
                new Price(entity.getPrice()),
                new EnginePower(entity.getEnginePower()),
                new EngineVolume(entity.getEngineVolume()),
                entity.getFuelType(),
                entity.getBody(),
                entity.getDrive()
        );
    }

    public CarModelEntity toEntity(CarModel model) {
        CarModelEntity entity = new CarModelEntity();
        entity.setId(model.getId());
        entity.setBrand(model.getBrand());
        entity.setModelName(model.getModelName());
        entity.setPrice(model.getPrice().getValue());
        entity.setEnginePower(model.getEnginePower().getValue());
        entity.setEngineVolume(model.getEngineVolume().getValue());
        entity.setFuelType(model.getFuel());
        entity.setBody(model.getBody());
        entity.setDrive(model.getDrive());
        return entity;
    }
}

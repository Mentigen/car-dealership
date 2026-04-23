package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.car.*;
import ru.CarDealership.infrastructure.entity.CarModelEntity;

import java.util.ArrayList;

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
        var entity = new CarModelEntity(
                model.getBrand(),
                model.getModelName(),
                model.getPrice().getValue(),
                model.getEnginePower().getValue(),
                model.getEngineVolume().getValue(),
                model.getFuel(),
                model.getBody(),
                model.getDrive(),
                new ArrayList<>()
        );
        entity.setId(model.getId());
        return entity;
    }
}

package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.order.TestDriveRequest;
import ru.CarDealership.infrastructure.entity.CarEntity;
import ru.CarDealership.infrastructure.entity.TestDriveRequestEntity;

@Component
public class TestDriveRequestEntityMapper {

    private final UserEntityMapper userMapper;
    private final CarEntityMapper carMapper;
    private final TestDriveStateFactory stateFactory;

    public TestDriveRequestEntityMapper(
        UserEntityMapper userMapper,
        CarEntityMapper carMapper,
        TestDriveStateFactory stateFactory
    ) {
        this.userMapper = userMapper;
        this.carMapper = carMapper;
        this.stateFactory = stateFactory;
    }

    public TestDriveRequest toDomain(TestDriveRequestEntity entity) {
        return new TestDriveRequest(
            entity.getId(),
            userMapper.toDomain(entity.getClient()),
            carMapper.toDomain(entity.getCar()),
            entity.getStartTime(),
            stateFactory.fromString(entity.getStatus())
        );
    }

    public TestDriveRequestEntity toEntity(TestDriveRequest testDriveRequest) {
        var entity = new TestDriveRequestEntity();
        entity.setId(testDriveRequest.getId());
        entity.setClient(userMapper.toEntity(testDriveRequest.getClient()));
        entity.setCar(new ru.CarDealership.infrastructure.entity.CarEntity());
        entity.getCar().setId(testDriveRequest.getCar().getId());
        entity.setStartTime(testDriveRequest.getStartTime());
        entity.setStatus(testDriveRequest.getState().getName());
        return entity;
    }
}

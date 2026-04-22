package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.order.TestDriveRequest;
import ru.CarDealership.infrastructure.entity.TestDriveRequestEntity;

@Component
public class TestDriveRequestEntityMapper {

    private final UserEntityMapper userMapper;
    private final TestDriveStateFactory stateFactory;

    public TestDriveRequestEntityMapper(UserEntityMapper userMapper, TestDriveStateFactory stateFactory) {
        this.userMapper = userMapper;
        this.stateFactory = stateFactory;
    }

    public TestDriveRequest toDomain(TestDriveRequestEntity entity) {
        return new TestDriveRequest(
                entity.getId(),
                userMapper.toDomain(entity.getClient()),
                entity.getCarId(),
                entity.getStartTime(),
                stateFactory.fromString(entity.getStatus())
        );
    }

    public TestDriveRequestEntity toEntity(TestDriveRequest request) {
        var entity = new TestDriveRequestEntity(
                userMapper.toEntity(request.getClient()),
                request.getCarId(),
                request.getStartTime(),
                request.getState().getName()
        );
        entity.setId(request.getId());
        return entity;
    }
}

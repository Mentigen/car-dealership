package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.order.*;
import ru.CarDealership.infrastructure.entity.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class OrderEntityMapper {

    private final UserEntityMapper userMapper;
    private final OrderStateFactory stateFactory;
    private final List<Map.Entry<Predicate<OrderEntity>, Function<OrderEntity, Order>>> creators;

    public OrderEntityMapper(UserEntityMapper userMapper, OrderStateFactory stateFactory) {
        this.userMapper = userMapper;
        this.stateFactory = stateFactory;

        this.creators = List.of(
                Map.entry(e -> e instanceof StockOrderEntity, this::createStockOrder),
                Map.entry(e -> e instanceof CustomOrderEntity, this::createCustomOrder)
        );
    }

    public OrderEntity toEntity(Order order) {
        OrderEntity entity;

        if (order instanceof StockOrder stockOrder) {
            StockOrderEntity stockEntity = new StockOrderEntity();
            stockEntity.setCarId(stockOrder.getCarId());
            entity = stockEntity;
        } else if (order instanceof CustomOrder customOrder) {
            CustomOrderEntity customEntity = new CustomOrderEntity();
            customEntity.setCarConfigurationId(customOrder.getCarConfigurationId());
            entity = customEntity;
        } else {
            throw new IllegalArgumentException("Unknown order type: " + order.getClass());
        }

        entity.setId(order.getId());
        entity.setStatus(order.getState().getName(order));

        UserEntity clientStub = new UserEntity();
        clientStub.setId(order.getClient().getId());
        entity.setClient(clientStub);

        UserEntity managerStub = new UserEntity();
        managerStub.setId(order.getManager().getId());
        entity.setManager(managerStub);

        entity.setPrice(order.getPrice());

        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        return creators.stream()
                .filter(entry -> entry.getKey().test(entity))
                .map(entry -> entry.getValue().apply(entity))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No creator found for " + entity.getClass()));
    }

    private Order createStockOrder(OrderEntity entity) {
        StockOrderEntity stockEntity = (StockOrderEntity) entity;
        return StockOrder.builder()
                .id(entity.getId())
                .state(stateFactory.fromString(entity.getStatus()))
                .client(userMapper.toDomain(entity.getClient()))
                .manager(userMapper.toDomain(entity.getManager()))
                .price(entity.getPrice())
                .carId(stockEntity.getCarId())
                .build();
    }

    private Order createCustomOrder(OrderEntity entity) {
        CustomOrderEntity customEntity = (CustomOrderEntity) entity;
        return CustomOrder.builder()
                .id(entity.getId())
                .state(stateFactory.fromString(entity.getStatus()))
                .client(userMapper.toDomain(entity.getClient()))
                .manager(userMapper.toDomain(entity.getManager()))
                .price(entity.getPrice())
                .carConfigurationId(customEntity.getCarConfigurationId())
                .build();
    }
}

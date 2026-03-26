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
    private final CarEntityMapper carMapper;
    private final CarConfigurationEntityMapper configMapper;
    private final OrderStateFactory stateFactory;

    private final List<Map.Entry<Predicate<OrderEntity>, Function<OrderEntity, Order>>> creators;

    public OrderEntityMapper(
            UserEntityMapper userMapper,
            CarEntityMapper carMapper,
            CarConfigurationEntityMapper configMapper,
            OrderStateFactory stateFactory
    ) {
        this.userMapper = userMapper;
        this.carMapper = carMapper;
        this.configMapper = configMapper;
        this.stateFactory = stateFactory;

        this.creators = List.of(
                Map.entry(e -> e instanceof StockOrderEntity, this::createStockOrder),
                Map.entry(e -> e instanceof CustomOrderEntity, this::createCustomOrder),
                Map.entry(e -> true, this::createOrder)
        );
    }

    public OrderEntity toEntity(Order order) {
        OrderEntity entity;

        if (order instanceof StockOrder stockOrder) {
            StockOrderEntity stockEntity = new StockOrderEntity();
            CarEntity carStub = new CarEntity();
            carStub.setId(stockOrder.getCar().getId());
            stockEntity.setCar(carStub);
            entity = stockEntity;
        } else if (order instanceof CustomOrder customOrder) {
            CustomOrderEntity customEntity = new CustomOrderEntity();
            CarConfigurationEntity configStub = new CarConfigurationEntity();
            configStub.setId(customOrder.getCarConfiguration().getId());
            customEntity.setCarConfiguration(configStub);
            entity = customEntity;
        } else {
            entity = new OrderEntity();
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
        throw new UnsupportedOperationException("Not implemented");
    }

    private Order createCustomOrder(OrderEntity entity) {
        CustomOrderEntity customEntity = (CustomOrderEntity) entity;
        throw new UnsupportedOperationException("Not implemented");
    }

    private Order createOrder(OrderEntity entity) {
        throw new IllegalArgumentException("Cannot create generic Order from entity, use subclass");
    }
}
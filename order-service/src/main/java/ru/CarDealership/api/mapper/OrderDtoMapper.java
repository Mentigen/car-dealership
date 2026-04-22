package ru.CarDealership.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.CarDealership.api.dto.CustomOrderResponse;
import ru.CarDealership.api.dto.OrderResponse;
import ru.CarDealership.api.dto.StockOrderResponse;
import ru.CarDealership.domain.order.CustomOrder;
import ru.CarDealership.domain.order.Order;
import ru.CarDealership.domain.order.StockOrder;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "manager.id", target = "managerId")
    @Mapping(expression = "java(order.getState().getName(order))", target = "status")
    StockOrderResponse toStockResponse(StockOrder order);

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "manager.id", target = "managerId")
    @Mapping(expression = "java(order.getState().getName(order))", target = "status")
    CustomOrderResponse toCustomResponse(CustomOrder order);

    default OrderResponse toPolymorphicResponse(Order order) {
        if (order instanceof StockOrder stockOrder) {
            return toStockResponse(stockOrder);
        } else if (order instanceof CustomOrder customOrder) {
            return toCustomResponse(customOrder);
        }
        throw new IllegalArgumentException("Unknown order type: " + order.getClass());
    }
}

package ru.CarDealership.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "orderType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StockOrderResponse.class, name = "STOCK"),
        @JsonSubTypes.Type(value = CustomOrderResponse.class, name = "CUSTOM")
})
public sealed interface OrderResponse permits StockOrderResponse, CustomOrderResponse {
    UUID id();
    UUID clientId();
    UUID managerId();
    BigDecimal price();
    String status();
}

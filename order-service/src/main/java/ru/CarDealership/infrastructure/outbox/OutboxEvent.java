package ru.CarDealership.infrastructure.outbox;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.CarDealership.domain.order.CustomOrder;
import ru.CarDealership.domain.order.Order;
import ru.CarDealership.domain.order.StockOrder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
@NoArgsConstructor
public class OutboxEvent {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private String orderType;

    @Column
    private UUID carId;

    @Column
    private UUID carConfigurationId;

    @Column(nullable = false)
    private UUID traceId;

    @Column(nullable = false)
    private Instant createdAt;

    @Setter
    @Column(nullable = false)
    private boolean sent;

    @Setter
    @Column
    private Instant sentAt;

    public static OutboxEvent of(Order order) {
        OutboxEvent event = new OutboxEvent();
        event.id = UUID.randomUUID();
        event.orderId = order.getId();
        event.traceId = UUID.randomUUID();
        event.createdAt = Instant.now();
        event.sent = false;

        if (order instanceof StockOrder stockOrder) {
            event.orderType = "STOCK";
            event.carId = stockOrder.getCarId();
        } else if (order instanceof CustomOrder customOrder) {
            event.orderType = "CUSTOM";
            event.carConfigurationId = customOrder.getCarConfigurationId();
        }

        return event;
    }
}

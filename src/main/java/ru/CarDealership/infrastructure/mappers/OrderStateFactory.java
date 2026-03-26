package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.order.state.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class OrderStateFactory {
    private final Map<String, Supplier<OrderState>> states = new HashMap<>();

    public OrderStateFactory() {
        states.put("CREATED_STOCK", CreatedStockState::new);
        states.put("CREATED_CUSTOM", CreatedCustomState::new);
        states.put("APPROVED_BY_MANAGER", ApprovedByManagerState::new);
        states.put("APPROVED_BY_WAREHOUSE", ApprovedByWarehouseState::new);
        states.put("AWAITING_PAYMENT", AwaitingPaymentState::new);
        states.put("AWAITING_DELIVERY", AwaitingDeliveryState::new);
        states.put("PAID", PaidState::new);
        states.put("READY_FOR_ISSUE", ReadyForIssueState::new);
        states.put("COMPLETED", CompletedState::new);
        states.put("CANCELLED", CancelledState::new);
    }

    public OrderState fromString(String state) {
        return states.getOrDefault(state, () -> {
            throw new IllegalArgumentException("Unknown order status: " + state);
        }).get();
    }
}

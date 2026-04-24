package ru.CarDealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.CarDealership.client.StorageServiceClient;
import ru.CarDealership.domain.order.Order;
import ru.CarDealership.domain.order.StockOrder;
import ru.CarDealership.domain.order.state.ReadyForIssueState;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.infrastructure.outbox.OutboxEvent;
import ru.CarDealership.infrastructure.outbox.OutboxEventRepository;
import ru.CarDealership.messaging.OrderEventConsumer;
import ru.CarDealership.messaging.event.OrderApprovedEvent;
import ru.CarDealership.service.OrderService;
import ru.CarDealership.service.UserService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderApprovedFlowIT extends BaseIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    OutboxEventRepository outboxEventRepository;

    @Autowired
    OrderEventConsumer orderEventConsumer;

    @MockBean
    StorageServiceClient storageServiceClient;

    @Test
    void orderApprovedEvent_advancesOrderToReadyForIssue() {
        User customer = userService.findByEmail("customer@mail.ru").orElseThrow();

        StockOrder order = orderService.createStockOrder(customer, UUID.randomUUID(), BigDecimal.valueOf(500_000));

        orderService.nextStep(order.getId());
        orderService.nextStep(order.getId());
        orderService.nextStep(order.getId());
        orderService.nextStep(order.getId());

        List<OutboxEvent> pending = outboxEventRepository.findBySentFalse();
        assertThat(pending).hasSize(1);
        assertThat(pending.get(0).getOrderId()).isEqualTo(order.getId());

        orderEventConsumer.handleOrderApproved(
                new OrderApprovedEvent(order.getId(), UUID.randomUUID(), UUID.randomUUID(), Instant.now())
        );

        Order updated = orderService.findOrderById(order.getId()).orElseThrow();
        assertThat(updated.getState()).isInstanceOf(ReadyForIssueState.class);
    }
}

package ru.CarDealership.service;

import ru.CarDealership.domain.order.CustomOrder;
import ru.CarDealership.domain.order.Order;
import ru.CarDealership.domain.order.StockOrder;
import ru.CarDealership.domain.order.state.AwaitingDeliveryState;
import ru.CarDealership.domain.order.state.ReadyForIssueState;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.infrastructure.outbox.OutboxEventRepository;
import ru.CarDealership.infrastructure.repository.InMemoryOrderRepository;
import ru.CarDealership.infrastructure.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderService orderService;
    private InMemoryOrderRepository orderRepository;
    private InMemoryUserRepository userRepository;
    private OutboxEventRepository outboxEventRepository;

    private User client;
    private User manager;

    @BeforeEach
    void setUp() {
        orderRepository = new InMemoryOrderRepository();
        userRepository = new InMemoryUserRepository();
        outboxEventRepository = mock(OutboxEventRepository.class);
        orderService = new OrderService(orderRepository, userRepository, outboxEventRepository);

        client = new User(UUID.randomUUID(), "Ivan", "Ivanov", Role.USER, "ivan@ya.ru", "88005553535", "ivan228");
        manager = new User(UUID.randomUUID(), "Petr", "Petrov", Role.MANAGER, "petr@gmail.com", "001", "petr007");

        userRepository.save(client);
        userRepository.save(manager);
    }

    @Test
    void testCreateStockOrder() {
        StockOrder order = orderService.createStockOrder(client, UUID.randomUUID(), BigDecimal.valueOf(3_000_000));

        assertNotNull(orderService.findOrderById(order.getId()));
        assertEquals("CREATED_STOCK", order.getState().getName(order));
    }

    @Test
    void testOrderLifecycle_NextStep() {
        StockOrder order = orderService.createStockOrder(client, UUID.randomUUID(), BigDecimal.valueOf(3_000_000));
        UUID id = order.getId();

        assertEquals("CREATED_STOCK", orderService.findOrderById(id).orElseThrow().getState().getName(order));

        orderService.nextStep(id);
        assertEquals("APPROVED_BY_MANAGER", orderService.findOrderById(id).orElseThrow().getState().getName(order));

        orderService.nextStep(id);
        assertEquals("APPROVED_BY_WAREHOUSE", orderService.findOrderById(id).orElseThrow().getState().getName(order));

        orderService.nextStep(id);
        assertEquals("AWAITING_PAYMENT", orderService.findOrderById(id).orElseThrow().getState().getName(order));

        orderService.nextStep(id);
        assertEquals("PAID", orderService.findOrderById(id).orElseThrow().getState().getName(order));

        orderService.nextStep(id);
        Order updated = orderService.findOrderById(id).orElseThrow();
        assertInstanceOf(ReadyForIssueState.class, updated.getState());

        orderService.nextStep(id);
        assertEquals("COMPLETED", orderService.findOrderById(id).orElseThrow().getState().getName(order));

        assertThrows(IllegalStateException.class, () -> orderService.cancelOrder(id));
    }

    @Test
    void testCreateOrderNoManagerException() {
        userRepository.delete(manager.getId());

        assertThrows(IllegalStateException.class,
                () -> orderService.createStockOrder(client, UUID.randomUUID(), BigDecimal.valueOf(3_000_000)));
    }

    @Test
    void testCustomOrderLifecycle() {
        CustomOrder order = orderService.createCustomOrder(client, UUID.randomUUID(), BigDecimal.valueOf(3_320_000));
        UUID id = order.getId();

        assertEquals("CREATED_CUSTOM", order.getState().getName(order));

        orderService.nextStep(id);
        orderService.nextStep(id);
        orderService.nextStep(id);
        orderService.nextStep(id);

        assertEquals("PAID", orderService.findOrderById(id).orElseThrow().getState().getName(order));

        orderService.markAsReadyForIssue(id);
        Order updated = orderService.findOrderById(id).orElseThrow();
        assertInstanceOf(AwaitingDeliveryState.class, updated.getState());
    }

    @Test
    void testCancelOrder() {
        StockOrder order = orderService.createStockOrder(client, UUID.randomUUID(), BigDecimal.valueOf(3_000_000));
        orderService.cancelOrder(order.getId());
        assertEquals("CANCELLED", orderService.findOrderById(order.getId()).orElseThrow().getState().getName(order));
    }
}

package ru.CarDealership.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.order.*;
import ru.CarDealership.domain.order.state.AwaitingPaymentState;
import ru.CarDealership.domain.order.state.CreatedCustomState;
import ru.CarDealership.domain.order.state.CreatedStockState;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;
import ru.CarDealership.infrastructure.outbox.OutboxEvent;
import ru.CarDealership.infrastructure.outbox.OutboxEventRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OutboxEventRepository outboxEventRepository;

    @Transactional(readOnly = true)
    public List<Order> findOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Order> findOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersVisibleTo(User user, boolean privileged) {
        if (privileged) return orderRepository.findAll();
        return orderRepository.findByCustomer(user);
    }

    public StockOrder createStockOrder(User client, UUID carId, BigDecimal price) {
        User manager = pickManager();
        StockOrder order = StockOrder.builder()
                .state(new CreatedStockState())
                .id(UUID.randomUUID())
                .client(client)
                .manager(manager)
                .price(price)
                .carId(carId)
                .build();
        orderRepository.save(order);
        return order;
    }

    public CustomOrder createCustomOrder(User client, UUID carConfigurationId, BigDecimal price) {
        User manager = pickManager();
        CustomOrder order = CustomOrder.builder()
                .state(new CreatedCustomState())
                .id(UUID.randomUUID())
                .client(client)
                .manager(manager)
                .price(price)
                .carConfigurationId(carConfigurationId)
                .build();
        orderRepository.save(order);
        return order;
    }

    public Order nextStep(UUID id) {
        Order order = findOrderById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        boolean wasAwaitingPayment = order.getState() instanceof AwaitingPaymentState;
        order.next();
        Order saved = orderRepository.save(order);
        if (wasAwaitingPayment) {
            outboxEventRepository.save(OutboxEvent.of(saved));
        }
        return saved;
    }

    public Order cancelOrder(UUID id) {
        Order order = findOrderById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.cancel();
        return orderRepository.save(order);
    }

    public void markAsReadyForIssue(UUID orderId) {
        Order order = findOrderById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.next();
        orderRepository.save(order);
    }

    public void markAsCancelled(UUID orderId) {
        Order order = findOrderById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.cancel();
        orderRepository.save(order);
    }

    private User pickManager() {
        List<User> managers = userRepository.findByRole(Role.MANAGER);
        if (managers.isEmpty()) throw new IllegalStateException("No managers available");
        return managers.get(new Random().nextInt(managers.size()));
    }
}

package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.car.Car;
import org.example.domain.car.CarConfiguration;
import org.example.domain.order.*;
import org.example.domain.order.state.CreatedCustomState;
import org.example.domain.order.state.CreatedStockState;
import org.example.domain.user.Role;
import org.example.domain.user.User;
import org.example.domain.user.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public List<Order> findOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> findOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public List<Order> findOrderByManager(User manager) {
        return orderRepository.findByManager(manager);
    }

    public List<Order> findOrdersByCustomer(User customer) {
        return orderRepository.findByCustomer(customer);
    }

    public StockOrder createStockOrder(User client, Car car) {
        List<User> managers = userRepository.findByRole(Role.MANAGER);
        if (managers.isEmpty()) throw new IllegalStateException("No managers available");
        User manager = managers.get(new Random().nextInt(managers.size()));

        StockOrder order = new StockOrder(new CreatedStockState(), UUID.randomUUID(), client, manager, car.getPrice(), car);

        orderRepository.save(order);

        return order;
    }

    public CustomOrder createCustomOrder(User client, CarConfiguration carConfiguration) {
        List<User> managers = userRepository.findByRole(Role.MANAGER);
        if (managers.isEmpty()) throw new IllegalStateException("No managers available");
        User manager = managers.get(new Random().nextInt(managers.size()));

        CustomOrder order = new CustomOrder(new CreatedCustomState(), UUID.randomUUID(), client, manager, carConfiguration.getPrice(), carConfiguration);

        orderRepository.save(order);

        return order;
    }

    public Order nextStep(UUID id) {
        Order order = findOrderById(id)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        order.next();

        return order;
    }

    public Order cancelOrder(UUID id) {
        Order order = findOrderById(id)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        order.cancel();

        return order;
    }
}

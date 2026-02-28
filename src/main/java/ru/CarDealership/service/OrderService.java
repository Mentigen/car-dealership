package ru.CarDealership.service;

import ru.CarDealership.domain.order.CustomOrder;
import ru.CarDealership.domain.order.Order;
import ru.CarDealership.domain.order.OrderRepository;
import ru.CarDealership.domain.order.StockOrder;
import lombok.AllArgsConstructor;
import ru.CarDealership.domain.car.Car;
import ru.CarDealership.domain.car.CarConfiguration;
import ru.CarDealership.domain.order.state.CreatedCustomState;
import ru.CarDealership.domain.order.state.CreatedStockState;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;

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

    StockOrder order =
        StockOrder.builder()
            .state(new CreatedStockState())
            .id(UUID.randomUUID())
            .client(client)
            .manager(manager)
            .price(car.getPrice())
            .car(car)
            .build();

    orderRepository.save(order);

    return order;
  }

  public CustomOrder createCustomOrder(User client, CarConfiguration carConfiguration) {
    List<User> managers = userRepository.findByRole(Role.MANAGER);
    if (managers.isEmpty()) throw new IllegalStateException("No managers available");
    User manager = managers.get(new Random().nextInt(managers.size()));

    CustomOrder order =
        CustomOrder.builder()
            .state(new CreatedCustomState())
            .id(UUID.randomUUID())
            .client(client)
            .manager(manager)
            .price(carConfiguration.getPrice())
            .carConfiguration(carConfiguration)
            .build();

    orderRepository.save(order);

    return order;
  }

  public Order nextStep(UUID id) {
    Order order = findOrderById(id).orElseThrow(() -> new IllegalStateException("Order not found"));

    order.next();

    return order;
  }

  public Order cancelOrder(UUID id) {
    Order order = findOrderById(id).orElseThrow(() -> new IllegalStateException("Order not found"));

    order.cancel();

    return order;
  }
}

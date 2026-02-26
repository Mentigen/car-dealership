package org.example.infrastructure.repository;

import org.example.domain.order.Order;
import org.example.domain.order.OrderRepository;
import org.example.domain.user.User;

import java.util.*;

public class InMemoryOrderRepository implements OrderRepository {
  private final HashMap<UUID, Order> storage = new HashMap<UUID, Order>();

  @Override
  public Optional<Order> findById(UUID id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public List<Order> findByCustomer(User customer) {
    return storage.values().stream().filter(order -> order.getClient().equals(customer)).toList();
  }

  @Override
  public List<Order> findByManager(User manager) {
    return storage.values().stream().filter(order -> order.getManager().equals(manager)).toList();
  }

  @Override
  public List<Order> findByStatus(String status) {
    return storage.values().stream()
        .filter(order -> order.getState().getName(order).equalsIgnoreCase(status))
        .toList();
  }

  @Override
  public Order save(Order order) {
    storage.put(order.getId(), order);
    return order;
  }

  @Override
  public void delete(UUID id) {
    storage.remove(id);
  }
}

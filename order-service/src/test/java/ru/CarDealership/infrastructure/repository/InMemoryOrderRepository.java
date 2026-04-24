package ru.CarDealership.infrastructure.repository;

import ru.CarDealership.domain.order.Order;
import ru.CarDealership.domain.order.OrderRepository;
import ru.CarDealership.domain.user.User;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<UUID, Order> storage = new HashMap<>();

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
        return storage.values().stream()
                .filter(o -> customer.equals(o.getClient()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByManager(User manager) {
        return storage.values().stream()
                .filter(o -> manager.equals(o.getManager()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(String statusName) {
        return storage.values().stream()
                .filter(o -> statusName.equals(o.getState().getName(o)))
                .collect(Collectors.toList());
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

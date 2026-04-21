package ru.CarDealership.domain.order;

import ru.CarDealership.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<Order> findById(UUID id);
    List<Order> findAll();
    List<Order> findByCustomer(User customer);
    List<Order> findByManager(User manager);
    List<Order> findByStatus(String statusName);
    Order save(Order order);
    void delete(UUID id);
}

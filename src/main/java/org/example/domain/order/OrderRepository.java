package org.example.domain.order;

import org.example.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    public Optional<Order> findById(UUID id);
    public List<Order> findAll();
    public List<Order> findByCustomer(User customer);
    public List<Order> findByManager(User manager);
    public List<Order> findByStatus(String statusName);
    public Order save(Order order);
    public void delete(UUID id);
}
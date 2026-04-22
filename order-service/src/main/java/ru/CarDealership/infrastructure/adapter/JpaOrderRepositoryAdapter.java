package ru.CarDealership.infrastructure.adapter;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.order.Order;
import ru.CarDealership.domain.order.OrderRepository;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.infrastructure.jpa.OrderJpaRepository;
import ru.CarDealership.infrastructure.mappers.OrderEntityMapper;
import ru.CarDealership.infrastructure.mappers.UserEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaOrderRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository jpaRepository;
    private final OrderEntityMapper mapper;
    private final UserEntityMapper userMapper;

    public JpaOrderRepositoryAdapter(
            OrderJpaRepository jpaRepository,
            OrderEntityMapper mapper,
            UserEntityMapper userMapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Order> findByCustomer(User customer) {
        return jpaRepository.findByClient(userMapper.toEntity(customer)).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public List<Order> findByManager(User manager) {
        return jpaRepository.findByManager(userMapper.toEntity(manager)).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public List<Order> findByStatus(String statusName) {
        return jpaRepository.findByStatus(statusName).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Order save(Order order) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(order)));
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(e -> {
            e.setRemoved(true);
            jpaRepository.save(e);
        });
    }
}

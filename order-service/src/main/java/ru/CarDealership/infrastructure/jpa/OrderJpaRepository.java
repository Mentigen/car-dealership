package ru.CarDealership.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.CarDealership.infrastructure.entity.OrderEntity;
import ru.CarDealership.infrastructure.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByClient(UserEntity client);
    List<OrderEntity> findByManager(UserEntity manager);
    List<OrderEntity> findByStatus(String status);
}

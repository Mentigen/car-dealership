package ru.CarDealership.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.infrastructure.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    List<UserEntity> findByEmail(String email);
    List<UserEntity> findByRole(Role role);
}

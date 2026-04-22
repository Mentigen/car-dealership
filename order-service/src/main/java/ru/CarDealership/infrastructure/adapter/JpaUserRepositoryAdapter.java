package ru.CarDealership.infrastructure.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;
import ru.CarDealership.infrastructure.jpa.UserJpaRepository;
import ru.CarDealership.infrastructure.mappers.UserEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<User> findByRole(Role role) {
        return jpaRepository.findByRole(role).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).stream().map(mapper::toDomain).toList();
    }

    @Override
    public User save(User user) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(user)));
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(e -> {
            e.setRemoved(true);
            jpaRepository.save(e);
        });
    }
}

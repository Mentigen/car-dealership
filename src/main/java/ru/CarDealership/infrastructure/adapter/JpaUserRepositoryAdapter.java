package ru.CarDealership.infrastructure.adapter;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;
import ru.CarDealership.infrastructure.jpa.UserJpaRepository;
import ru.CarDealership.infrastructure.mappers.UserEntityMapper;

import java.util.List;

@Component
public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;

    public JpaUserRepositoryAdapter(UserJpaRepository jpaRepository, UserEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        return jpaRepository.findAll().stream()
                .filter(e -> e.getFirstName().equals(firstName))
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByLastName(String lastName) {
        return jpaRepository.findAll().stream()
                .filter(e -> e.getLastName().equals(lastName))
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByRole(Role role) {
        return jpaRepository.findByRole(role).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByName(String firstName, String lastName) {
        return jpaRepository.findAll().stream()
                .filter(e -> e.getFirstName().equals(firstName) && e.getLastName().equals(lastName))
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByPhone(String phone) {
        return jpaRepository.findAll().stream()
                .filter(e -> phone.equals(e.getPhone()))
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        var entity = mapper.toEntity(user);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(String email) {
        jpaRepository.findByEmail(email).stream()
                .forEach(e -> jpaRepository.delete(e));
    }
}
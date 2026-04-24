package ru.CarDealership.infrastructure.repository;

import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryUserRepository implements UserRepository {
    private final Map<UUID, User> storage = new HashMap<>();

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<User> findByRole(Role role) {
        return storage.values().stream()
                .filter(u -> role.equals(u.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByEmail(String email) {
        return storage.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }
}

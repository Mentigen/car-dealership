package ru.CarDealership.domain.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    List<User> findAll();
    List<User> findByRole(Role role);
    List<User> findByEmail(String email);
    User save(User user);
    void delete(UUID id);
}

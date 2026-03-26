package ru.CarDealership.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public User findById(UUID id) {
    return userRepository.findAll().stream()
            .filter(u -> u.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
  }

  public List<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public List<User> findByRole(Role role) {
    return userRepository.findByRole(role);
  }

  public User register(
      String email, String password, String firstName, String lastName, String phone, Role role) {
    User user = new User(UUID.randomUUID(), firstName, lastName, role, email, phone, password);

    return userRepository.save(user);
  }
}

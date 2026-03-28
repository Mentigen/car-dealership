package ru.CarDealership.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public User findById(UUID id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
  }

  @Transactional(readOnly = true)
  public List<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Transactional(readOnly = true)
  public List<User> findByRole(Role role) {
    return userRepository.findByRole(role);
  }

  public User register(
      String email, String password, String firstName, String lastName, String phone, Role role) {
    User user = new User(UUID.randomUUID(), firstName, lastName, role, email, phone, password);

    return userRepository.save(user);
  }
}

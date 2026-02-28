package CarDealership.service;

import lombok.AllArgsConstructor;
import CarDealership.domain.user.Role;
import CarDealership.domain.user.User;
import CarDealership.domain.user.UserRepository;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public List<User> findAll() {
    return userRepository.findAll();
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

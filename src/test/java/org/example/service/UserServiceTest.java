package org.example.service;

import org.example.domain.exceptions.EntityNotFoundException;
import org.example.domain.user.Role;
import org.example.domain.user.User;
import org.example.infrastructure.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

  private UserService userService;
  private InMemoryUserRepository userRepository;
  private User user;

  @BeforeEach
  void setUp() {
    userRepository = new InMemoryUserRepository();
    userService = new UserService(userRepository);

    user = new User(UUID.randomUUID(), "Ivan", "Ivanov", Role.CUSTOMER, "ivan@ya.ru", "123", "pass");
  }

  @Test
  void testRegisterUser() {
    User registered =
        userService.register("ivan@ya.ru", "pass", "Ivan", "Ivanov", "123", Role.CUSTOMER);

    assertNotNull(registered);
    assertEquals("Ivan", registered.getFirstName());

    List<User> found = userRepository.findByEmail("ivan@ya.ru");
    assertFalse(found.isEmpty());
    assertEquals(registered, found.get(0));
  }

  @Test
  void testFindUserByEmail() {
    userService.register("ivan@ya.ru", "pass", "Ivan", "Ivanov", "123", Role.CUSTOMER);

    List<User> found = userService.findByEmail("ivan@ya.ru");
    assertEquals(1, found.size());
    assertEquals("ivan@ya.ru", found.get(0).getEmail());
  }

  @Test
  void testFindUserByRole() {
    userService.register("ivan@ya.ru", "pass", "Ivan", "Ivanov", "123", Role.CUSTOMER);
    userService.register("petr@ya.ru", "pass", "Petr", "Petrov", "456", Role.MANAGER);

    List<User> customers = userService.findByRole(Role.CUSTOMER);
    List<User> managers = userService.findByRole(Role.MANAGER);

    assertEquals(1, customers.size());
    assertEquals(1, managers.size());
  }

  @Test
  void testFindAll() {
    userService.register("ivan@ya.ru", "pass", "Ivan", "Ivanov", "123", Role.CUSTOMER);
    assertFalse(userService.findAll().isEmpty());
  }
}

package ru.CarDealership.service;

import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.infrastructure.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
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

    user =
        new User(UUID.randomUUID(), "Ivan", "Ivanov", Role.USER, "ivan@ya.ru", "123", "pass");
  }

  @Test
  void testRegisterUser() {
    User registered =
        userService.register("ivan@ya.ru", "pass", "Ivan", "Ivanov", "123", Role.USER);

    assertNotNull(registered);
    assertEquals("Ivan", registered.getFirstName());

    List<User> found = userRepository.findByEmail("ivan@ya.ru");
    assertFalse(found.isEmpty());
    assertEquals(registered, found.get(0));
  }

  @Test
  void testFindUserByEmail() {
    userService.register("ivan@ya.ru", "pass", "Ivan", "Ivanov", "123", Role.USER);

    List<User> found = userService.findByEmail("ivan@ya.ru");
    assertEquals(1, found.size());
    assertEquals("ivan@ya.ru", found.get(0).getEmail());
  }

  @Test
  void testFindUserByRole() {
    userService.register("ivan@ya.ru", "pass", "Ivan", "Ivanov", "123", Role.USER);
    userService.register("petr@ya.ru", "pass", "Petr", "Petrov", "456", Role.MANAGER);

    List<User> customers = userService.findByRole(Role.USER);
    List<User> managers = userService.findByRole(Role.MANAGER);

    assertEquals(1, customers.size());
    assertEquals(1, managers.size());
  }

  @Test
  void testFindAll() {
    userService.register("ivan@ya.ru", "pass", "Ivan", "Ivanov", "123", Role.USER);
    assertFalse(userService.findAll().isEmpty());
  }
}

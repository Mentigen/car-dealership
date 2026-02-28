package ru.CarDealership.infrastructure.repository;

import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {
  private final HashMap<String, User> storage = new HashMap<>();

  @Override
  public User save(User user) {
    storage.put(user.getEmail(), user);
    return user;
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<User>(storage.values());
  }

  @Override
  public List<User> findByFirstName(String firstName) {
    return storage.values().stream().filter(user -> user.getFirstName().equals(firstName)).toList();
  }

  @Override
  public List<User> findByLastName(String lastName) {
    return storage.values().stream().filter(user -> user.getLastName().equals(lastName)).toList();
  }

  @Override
  public List<User> findByRole(Role role) {
    return storage.values().stream().filter(user -> user.getRole().equals(role)).toList();
  }

  @Override
  public List<User> findByName(String firstName, String lastName) {
    return storage.values().stream()
        .filter(
            user -> user.getFirstName().equals(firstName) && user.getLastName().equals(lastName))
        .toList();
  }

  public List<User> findByEmail(String email) {
    return storage.values().stream().filter(user -> user.getEmail().equals(email)).toList();
  }

  public List<User> findByPhone(String phone) {
    return storage.values().stream().filter(user -> user.getPhone().equals(phone)).toList();
  }

  @Override
  public void delete(String email) {
    storage.remove(email);
  }
}

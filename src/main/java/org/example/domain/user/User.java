package org.example.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "email")
public class User {
  private final String firstName;
  private final String lastName;
  private final Role role;
  private final String email;
  private final String phone;
  private final String passwordHash;

  public User(
      String firstName,
      String lastName,
      Role role,
      String email,
      String phone,
      String passwordHash) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
    this.email = email;
    this.phone = phone;
    this.passwordHash = passwordHash;
  }
}

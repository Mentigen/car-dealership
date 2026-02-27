package org.example.domain.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "email")
public class User {
  private final UUID id;
  private final String firstName;
  private final String lastName;
  private final Role role;
  private final String email;
  private final String phone;
  private final String passwordHash;
}

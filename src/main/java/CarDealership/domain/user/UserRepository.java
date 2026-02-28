package CarDealership.domain.user;

import java.util.List;

public interface UserRepository {
  public List<User> findAll();

  public List<User> findByFirstName(String firstName);

  public List<User> findByLastName(String lastName);

  public List<User> findByRole(Role role);

  public List<User> findByName(String firstName, String lastName);

  public List<User> findByEmail(String email);

  public List<User> findByPhone(String phone);

  public User save(User user);

  public void delete(String email);
}

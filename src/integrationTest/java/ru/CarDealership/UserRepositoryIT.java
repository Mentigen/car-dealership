package ru.CarDealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.CarDealership.domain.user.Role;
import ru.CarDealership.domain.user.User;
import ru.CarDealership.domain.user.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAll_returnsSeededUsers() {
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertTrue(users.size() >= 4);
    }

    @Test
    void findByRole_returnsManagers() {
        List<User> managers = userRepository.findByRole(Role.MANAGER);
        assertFalse(managers.isEmpty());
        managers.forEach(u -> assertEquals(Role.MANAGER, u.getRole()));
    }

    @Test
    void saveAndFind_worksCorrectly() {
        User user = new User(
                UUID.randomUUID(), "Test", "User", Role.USER,
                "test_" + UUID.randomUUID() + "@test.ru", "+70001112233", "hash123"
        );

        User saved = userRepository.save(user);
        assertNotNull(saved.getId());

        List<User> found = userRepository.findByEmail(saved.getEmail());
        assertFalse(found.isEmpty());
        assertEquals(saved.getEmail(), found.get(0).getEmail());
    }
}

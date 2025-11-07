package edu.az.example.web.travelplanning.repository;


import edu.az.example.web.travelplanning.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static edu.az.example.web.travelplanning.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .age(13)
                .gender(MALE)
                .build();

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedBy("test-user");
        user.setUpdatedBy("test-user");
    }

    @Test
    void testFindByName() {
        userRepository.save(user);
        List<User> foundUsers = userRepository.findByName("John");
        assertFalse(foundUsers.isEmpty());
        User foundUser = foundUsers.get(0);
        assertEquals(user.getName(), foundUser.getName());
    }

    @Test
    void testFindAllByAge() {
        userRepository.save(user);
        List<User> foundUsers = userRepository.findAllByAge(13);
        assertFalse(foundUsers.isEmpty());
        User foundUser = foundUsers.get(0);
        assertEquals(user.getName(), foundUser.getName());
    }

    @Test
    void testFindAllByGender() {
        userRepository.save(user);
        List<User> foundUsers = userRepository.findAllByGender(MALE);
        assertFalse(foundUsers.isEmpty());
        User foundUser = foundUsers.get(0);
        assertEquals(user.getName(), foundUser.getName());
    }

}
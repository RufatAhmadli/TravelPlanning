package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.enums.Gender;
import edu.az.example.web.travelplanning.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static edu.az.example.web.travelplanning.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .age(13)
                .gender(MALE).build();
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
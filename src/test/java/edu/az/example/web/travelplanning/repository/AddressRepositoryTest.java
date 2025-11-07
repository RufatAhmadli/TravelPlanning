package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static edu.az.example.web.travelplanning.enums.AddressType.HOME;
import static edu.az.example.web.travelplanning.enums.AddressType.WORK;
import static edu.az.example.web.travelplanning.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AddressRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AddressRepository addressRepository;

    private Address address;

    @BeforeEach
    void setUp() {
        User user = User.builder()
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
        entityManager.persistAndFlush(user);

        address = Address.builder()
                .street("Haydar Aliyev")
                .streetNumber("123A")
                .city("Baku")
                .postalCode("AZ100")
                .addressType(HOME)
                .user(user)
                .build();
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        address.setCreatedBy("test-user");
        address.setUpdatedBy("test-user");
    }

    @Test
    void testFindAllByCityIgnoreCase() {
        entityManager.persistAndFlush(address);
        entityManager.clear();

        List<Address> res = addressRepository.findAllByCityIgnoreCase("baku");
        assertFalse(res.isEmpty());
        Address foundAddress = res.get(0);
        assertEquals(foundAddress.getId(), address.getId());
        assertEquals(foundAddress.getStreet(), address.getStreet());
    }

    @Test
    void testFindAllByCityIgnoreCase_shouldReturnEmpty_whenCityNotFound() {
        entityManager.persistAndFlush(address);
        entityManager.clear();

        List<Address> res = addressRepository.findAllByCityIgnoreCase("Ganja");
        assertTrue(res.isEmpty());
    }

    @Test
    void findAllByAddressType() {
        entityManager.persistAndFlush(address);
        entityManager.clear();

        List<Address> res = addressRepository.findAllByAddressType(HOME);
        assertFalse(res.isEmpty());
        Address foundAddress = res.get(0);
        assertEquals(foundAddress.getId(), address.getId());
        assertEquals(foundAddress.getStreet(), address.getStreet());
        assertEquals(foundAddress.getAddressType(), address.getAddressType());
    }

    @Test
    void testFindAllByAddressTypeIgnoreCase_shouldReturnEmpty_whenTypeNotFound() {
        entityManager.persistAndFlush(address);
        entityManager.clear();

        List<Address> res = addressRepository.findAllByAddressType(WORK);
        assertTrue(res.isEmpty());
    }

    @Test
    void findAllByUserId() {
        entityManager.persistAndFlush(address);
        entityManager.clear();

        List<Address> res = addressRepository.findAllByUserId(address.getUser().getId());
        assertFalse(res.isEmpty());
        Address foundAddress = res.get(0);
        assertEquals(foundAddress.getId(), address.getId());
        assertEquals(foundAddress.getStreet(), address.getStreet());
        assertEquals(foundAddress.getAddressType(), address.getAddressType());
    }

    @Test
    void findAllByStreet() {
        entityManager.persistAndFlush(address);
        entityManager.clear();

        List<Address> res = addressRepository.findAllByStreet(address.getStreet(), address.getStreetNumber());
        assertFalse(res.isEmpty());
        Address foundAddress = res.get(0);
        assertEquals(foundAddress.getId(), address.getId());
        assertEquals(foundAddress.getStreet(), address.getStreet());
        assertEquals(foundAddress.getStreetNumber(), address.getStreetNumber());
    }
}
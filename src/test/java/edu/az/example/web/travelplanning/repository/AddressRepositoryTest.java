package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static edu.az.example.web.travelplanning.enums.AddressType.HOME;
import static edu.az.example.web.travelplanning.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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
        entityManager.persistAndFlush(user);

        address=Address.builder()
                .street("Haydar Aliyev")
                .streetNumber("123A")
                .city("Baku")
                .postalCode("AZ100")
                .addressType(HOME)
                .user(user)
                .build();
    }

    @Test
    void testFindAllByCityIgnoreCase() {
        addressRepository.save(address);
        List<Address> res = addressRepository.findAllByCityIgnoreCase(address.getCity());
        assertFalse(res.isEmpty());
        Address foundAddress=res.get(0);
        assertEquals(foundAddress.getId(), address.getId());
        assertEquals(foundAddress.getStreet(), address.getStreet());
    }

    @Test
    void findAllByAddressType() {
        addressRepository.save(address);
        List<Address> res = addressRepository.findAllByAddressType(address.getAddressType());
        assertFalse(res.isEmpty());
        Address foundAddress=res.get(0);
        assertEquals(foundAddress.getId(), address.getId());
        assertEquals(foundAddress.getStreet(), address.getStreet());
        assertEquals(foundAddress.getAddressType(), address.getAddressType());
    }

    @Test
    void findAllByUserId() {
        addressRepository.save(address);
        List<Address> res = addressRepository.findAllByUserId(address.getUser().getId());
        assertFalse(res.isEmpty());
        Address foundAddress=res.get(0);
        assertEquals(foundAddress.getId(), address.getId());
        assertEquals(foundAddress.getStreet(), address.getStreet());
        assertEquals(foundAddress.getAddressType(), address.getAddressType());
    }

    @Test
    void findAllByStreet() {
        addressRepository.save(address);
        List<Address> res = addressRepository.findAllByStreet(address.getStreet(), address.getStreetNumber());
        assertFalse(res.isEmpty());
        Address foundAddress=res.get(0);
        assertEquals(foundAddress.getId(), address.getId());
        assertEquals(foundAddress.getStreet(), address.getStreet());
        assertEquals(foundAddress.getStreetNumber(), address.getStreetNumber());
    }
}
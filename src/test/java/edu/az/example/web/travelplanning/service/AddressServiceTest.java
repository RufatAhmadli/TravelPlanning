package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.TestAuditable;
import edu.az.example.web.travelplanning.exception.custom.AddressNotFoundException;
import edu.az.example.web.travelplanning.mapper.AddressMapper;
import edu.az.example.web.travelplanning.dto.AddressDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.repository.AddressRepository;
import edu.az.example.web.travelplanning.security.UserSecurity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.az.example.web.travelplanning.enums.AddressType.HOME;
import static edu.az.example.web.travelplanning.enums.AddressType.WORK;
import static edu.az.example.web.travelplanning.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest implements TestAuditable {
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private UserSecurity userSecurity;
    @InjectMocks
    private AddressService addressService;

    private Address address;
    private AddressDto addressDto;
    private AddressDto updatedDto;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .age(13)
                .gender(MALE)
                .addresses(new ArrayList<>())
                .build();

        assignAuditFields(user);

        address = Address.builder()
                .id(1L)
                .street("Haydar Aliyev")
                .streetNumber("123A")
                .city("Baku")
                .postalCode("AZ100")
                .addressType(HOME)
                .user(user)
                .build();
        assignAuditFields(address);

        addressDto = AddressDto.builder()
                .id(1L)
                .street("Haydar Aliyev")
                .streetNumber("123A")
                .city("Baku")
                .postalCode("AZ100")
                .type(HOME)
                .build();

        updatedDto = AddressDto.builder()
                .id(1L)
                .street("Fuzuli Street")
                .streetNumber("123B")
                .city("Gabala")
                .postalCode("AZ100")
                .type(WORK)
                .build();
    }

    @Test
    void testFindAll() {
        when(addressRepository.findAll()).thenReturn(List.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);

        List<AddressDto> res = addressService.findAll();
        assertNotNull(res);
        AddressDto foundDto = res.get(0);
        assertEquals(address.getId(), foundDto.getId());
        assertEquals(address.getStreet(), foundDto.getStreet());
        assertEquals(address.getStreetNumber(), foundDto.getStreetNumber());
    }

    @Test
    void testFindById_whenUserIsOwner() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);
        when(userSecurity.isOwner(address.getUser().getId())).thenReturn(true);

        AddressDto foundDto = addressService.findById(1L);
        assertEquals(address.getId(), foundDto.getId());
        assertEquals(address.getStreet(), foundDto.getStreet());
        assertEquals(address.getStreetNumber(), foundDto.getStreetNumber());
    }

    @Test
    void testFindById_whenUserIsNotOwner() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(userSecurity.isOwner(address.getUser().getId())).thenReturn(false);

        assertThrows(SecurityException.class, () -> addressService.findById(1L));
    }

    @Test
    void testFindById_notFound() {
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(AddressNotFoundException.class, () -> addressService.findById(99L));
    }

    @Test
    void testFindAllByCity() {
        when(addressRepository.findAllByCityIgnoreCase(address.getCity())).thenReturn(List.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);

        List<AddressDto> res = addressService.findAllByCity(address.getCity());
        assertNotNull(res);
        AddressDto foundDto = res.get(0);
        assertEquals(address.getId(), foundDto.getId());
        assertEquals(address.getStreet(), foundDto.getStreet());
        assertEquals(address.getStreetNumber(), foundDto.getStreetNumber());
    }

    @Test
    void testFindAllByAddressType() {
        when(addressRepository.findAllByAddressType(HOME)).thenReturn(List.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);

        List<AddressDto> res = addressService.findAllByAddressType(HOME);
        assertNotNull(res);
        AddressDto foundDto = res.get(0);
        assertEquals(address.getId(), foundDto.getId());
        assertEquals(address.getStreet(), foundDto.getStreet());
        assertEquals(address.getStreetNumber(), foundDto.getStreetNumber());
        assertEquals(address.getAddressType(), foundDto.getType());
    }

    @Test
    void testFindAllByStreet() {
        when(addressRepository.findAllByStreet(address.getStreet(), address.getStreetNumber())).
                thenReturn(List.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);

        List<AddressDto> res = addressService.findAllByStreet(address.getStreet(), address.getStreetNumber());
        assertNotNull(res);
        AddressDto foundDto = res.get(0);
        assertEquals(address.getId(), foundDto.getId());
        assertEquals(address.getStreet(), foundDto.getStreet());
        assertEquals(address.getStreetNumber(), foundDto.getStreetNumber());
        assertEquals(address.getAddressType(), foundDto.getType());
    }

    @Test
    void testFindAddressesByUserId() {
        when(addressRepository.findAllByUserId(1L)).thenReturn(List.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);

        List<AddressDto> res = addressService.findAddressesByUserId(1L);
        assertNotNull(res);
        AddressDto foundDto = res.get(0);
        assertEquals(address.getId(), foundDto.getId());
        assertEquals(address.getStreet(), foundDto.getStreet());
        assertEquals(address.getStreetNumber(), foundDto.getStreetNumber());
        assertEquals(address.getAddressType(), foundDto.getType());
    }

    @Test
    void testCreate() {
        when(addressMapper.toAddressEntity(any(AddressDto.class))).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressMapper.toAddressDto(any(Address.class))).thenReturn(addressDto);
        when(userSecurity.getCurrentUser()).thenReturn(address.getUser());

        AddressDto res = addressService.create(addressDto);
        ArgumentCaptor<Address> captor = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository).save(captor.capture());
        Address value = captor.getValue();
        assertEquals(value.getId(), res.getId());
        assertEquals(value.getStreet(), res.getStreet());
        assertEquals(value.getStreetNumber(), res.getStreetNumber());
        assertEquals(value.getAddressType(), res.getType());

        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testCreateNullDto() {
        assertThrows(IllegalArgumentException.class, () -> addressService.create(null));
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void testUpdate_whenUserIsOwner() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(updatedDto);
        doNothing().when(addressMapper).updateAddressEntity(address, addressDto);
        when(addressRepository.save(address)).thenReturn(address);
        when(userSecurity.isOwner(address.getUser().getId())).thenReturn(true);

        AddressDto res = addressService.update(1L, addressDto);
        assertEquals(1L, res.getId());
        assertEquals("Fuzuli Street", res.getStreet());
        assertEquals("123B", res.getStreetNumber());
        assertEquals(WORK, res.getType());
    }

    @Test
    void testUpdate_UserIsNotOwner() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(userSecurity.isOwner(address.getUser().getId())).thenReturn(false);

        assertThrows(SecurityException.class, () -> addressService.update(1L, addressDto));
    }

    @Test
    void testUpdate_notFound() {
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(AddressNotFoundException.class, () -> addressService.update(99L, addressDto));
    }

    @Test
    void testUpdateNullDto() {
        assertThrows(IllegalArgumentException.class, () -> addressService.update(1L, null));
        verifyNoInteractions(addressRepository);
    }

    @Test
    void testDeleteById_whenUserIsOwner() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(userSecurity.isOwner(address.getUser().getId())).thenReturn(true);

        addressService.deleteById(1L);
        verify(addressRepository).deleteById(1L);
    }

    @Test
    void testDeleteById_whenUserIsNotOwner() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(userSecurity.isOwner(address.getUser().getId())).thenReturn(false);

        assertThrows(SecurityException.class, () -> addressService.deleteById(1L));
        verify(addressRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteById_notFound() {
        when(addressRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(AddressNotFoundException.class, () -> addressService.deleteById(999L));
    }
}
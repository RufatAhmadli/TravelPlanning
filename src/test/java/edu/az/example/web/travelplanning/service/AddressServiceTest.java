package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.mapper.AddressMapper;
import edu.az.example.web.travelplanning.dto.AddressDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static edu.az.example.web.travelplanning.enums.AddressType.HOME;
import static edu.az.example.web.travelplanning.enums.AddressType.WORK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressMapper addressMapper;
    @InjectMocks
    private AddressService addressService;

    private Address address;
    private AddressDto addressDto;
    private AddressDto updatedDto;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .id(1L)
                .street("Haydar Aliyev")
                .streetNumber("123A")
                .city("Baku")
                .postalCode("AZ100")
                .addressType(HOME)
                .build();

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
    void testFindById() {
        when(addressRepository.findById(1L)).thenReturn(java.util.Optional.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);

        AddressDto foundDto = addressService.findById(1L);
        assertEquals(address.getId(), foundDto.getId());
        assertEquals(address.getStreet(), foundDto.getStreet());
        assertEquals(address.getStreetNumber(), foundDto.getStreetNumber());
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
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressEntity(addressDto)).thenReturn(address);

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
    void testUpdate() {
        when(addressRepository.findById(1L)).thenReturn(java.util.Optional.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(updatedDto);
        doNothing().when(addressMapper).updateAddressEntity(address, addressDto);
        when(addressRepository.save(address)).thenReturn(address);

        AddressDto res = addressService.update(1L, addressDto);
        assertEquals(1L, res.getId());
        assertEquals("Fuzuli Street", res.getStreet());
        assertEquals("123B", res.getStreetNumber());
        assertEquals(WORK, res.getType());
    }

    @Test
    void testUpdateNullId() {
        assertThrows(IllegalArgumentException.class, () -> addressService.update(null, addressDto));
        verifyNoInteractions(addressRepository);

    }

    @Test
    void testUpdateNullDto() {
        assertThrows(IllegalArgumentException.class, () -> addressService.update(1L, null));
        verifyNoInteractions(addressRepository);
    }

    @Test
    void testUpdateNonExistentDto() {
        when(addressRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.update(1L, addressDto));
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void testDeleteById() {
        when(addressRepository.existsById(1L)).thenReturn(true);
        addressService.deleteById(1L);
        verify(addressRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when id is null")
    void testDeleteNullId() {
        assertThrows(IllegalArgumentException.class, () -> addressService.deleteById(null));
    }

    @Test
    @DisplayName("Should throw exception when address is not found")
    void testDeleteNotFoundAddress() {
        when(addressRepository.existsById(999L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> addressService.deleteById(999L));
    }
}
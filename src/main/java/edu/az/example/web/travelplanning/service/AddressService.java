package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.enums.AddressType;
import edu.az.example.web.travelplanning.dto.AddressDto;
import edu.az.example.web.travelplanning.exception.AddressNotFoundException;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.mapper.AddressMapper;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.repository.AddressRepository;
import edu.az.example.web.travelplanning.security.UserSecurity;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserSecurity userSecurity;

    @Transactional(readOnly = true)
    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toAddressDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AddressDto findById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));
        if (!userSecurity.isOwner(address.getUser().getId())) {
            throw new SecurityException("User is not owner of this address");
        }
        return addressMapper.toAddressDto(address);
    }

    @Transactional(readOnly = true)
    public List<AddressDto> findAllByCity(String city) {
        return addressRepository.findAllByCityIgnoreCase(city)
                .stream().map(addressMapper::toAddressDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AddressDto> findAllByAddressType(AddressType addressType) {
        return addressRepository.findAllByAddressType(addressType)
                .stream()
                .map(addressMapper::toAddressDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AddressDto> findAllByStreet(String street, String streetNumber) {
        return addressRepository.findAllByStreet(street, streetNumber)
                .stream()
                .map(addressMapper::toAddressDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AddressDto> findAddressesByUserId(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        return addresses.stream()
                .map(addressMapper::toAddressDto)
                .toList();
    }

    @Transactional
    public AddressDto create(AddressDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Address dto cannot be null");
        }
        Address address = addressMapper.toAddressEntity(dto);
        User currentUser = userSecurity.getCurrentUser();
        address.setUser(currentUser);
        currentUser.addAddress(address);
        addressRepository.save(address);
        return addressMapper.toAddressDto(address);
    }

    @Transactional
    public AddressDto update(Long id, AddressDto dto) {
        if (dto == null)
            throw new IllegalArgumentException("Address dto cannot be null");
        Address address = addressRepository.findById(id).
                orElseThrow(() -> new AddressNotFoundException(id));
        if (!userSecurity.isOwner(address.getUser().getId())) {
            throw new SecurityException("Cannot update address that doesn't belong to you");
        }
        addressMapper.updateAddressEntity(address, dto);
        addressRepository.save(address);
        return addressMapper.toAddressDto(address);
    }

    @Transactional
    public void deleteById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));
        if (!userSecurity.isOwner(address.getUser().getId())) {
            throw new SecurityException("Cannot update address that doesn't belong to you");
        }
        addressRepository.deleteById(id);
    }


}

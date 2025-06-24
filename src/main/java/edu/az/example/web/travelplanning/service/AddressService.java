package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.enums.AddressType;
import edu.az.example.web.travelplanning.model.dto.AddressDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.mapper.AddressMapper;
import edu.az.example.web.travelplanning.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toAddressDto)
                .toList();
    }

    public AddressDto findById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toAddressDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<AddressDto> findAllByCity(String city) {
        return addressRepository.findAllByCityIgnoreCase(city)
                .stream().map(addressMapper::toAddressDto)
                .toList();
    }

    public List<AddressDto> findAllByAddressType(AddressType addressType) {
        return addressRepository.findAllByAddressType(addressType)
                .stream()
                .map(addressMapper::toAddressDto)
                .toList();
    }

    public List<AddressDto> findAllByStreet(String street, String streetNumber) {
        return addressRepository.findAllByStreet(street, streetNumber)
                .stream()
                .map(addressMapper::toAddressDto)
                .toList();
    }

    public List<AddressDto> findAddressesByUserId(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        return addresses.stream()
                .map(addressMapper::toAddressDto)
                .toList();
    }

    public AddressDto create(AddressDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException();
        }
        Address savedAddress = addressRepository.save(addressMapper.toAddress(dto));
        return addressMapper.toAddressDto(savedAddress);
    }

    public AddressDto update(Long id, AddressDto dto) {
        if (id == null || dto == null) throw new IllegalArgumentException();
        Address address = addressRepository.findById(id).
                orElseThrow(EntityNotFoundException::new);
        addressMapper.toAddressEntity(address, dto);
        addressRepository.save(address);
        return addressMapper.toAddressDto(address);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        if (!addressRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        addressRepository.deleteById(id);
    }


}

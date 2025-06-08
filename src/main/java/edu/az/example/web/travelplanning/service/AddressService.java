package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.enums.AddressType;
import edu.az.example.web.travelplanning.model.dto.AddressDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.mapper.AddressMapper;
import edu.az.example.web.travelplanning.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

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

    public AddressDto create(AddressDto dto) {
        Address savedAddress = addressRepository.save(addressMapper.toAddress(dto));
        return addressMapper.toAddressDto(savedAddress);
    }

    public AddressDto update(AddressDto dto, Long id) {
        Address address = addressRepository.findById(id).
                orElseThrow(EntityNotFoundException::new);
        addressMapper.toAddressEntity(address, dto);
        addressRepository.save(address);
        return addressMapper.toAddressDto(address);
    }

    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }


}

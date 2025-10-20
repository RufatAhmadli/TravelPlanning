package edu.az.example.web.travelplanning.controller;


import edu.az.example.web.travelplanning.enums.AddressType;
import edu.az.example.web.travelplanning.dto.AddressDto;
import edu.az.example.web.travelplanning.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/addresses")
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AddressDto>> getAddresses() {
        return ResponseEntity.ok(addressService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/city/{city}")
    public ResponseEntity<List<AddressDto>> getAddressesByCity(@PathVariable String city) {
        return ResponseEntity.ok(addressService.findAllByCity(city));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/addressType/{addressType}")
    public ResponseEntity<List<AddressDto>> getAddressesByAddressType(@PathVariable AddressType addressType) {
        return ResponseEntity.ok(addressService.findAllByAddressType(addressType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/street")
    public ResponseEntity<List<AddressDto>> getAllAddressesByStreet(@RequestParam String street, @RequestParam String number) {
        return ResponseEntity.ok(addressService.findAllByStreet(street, number));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> getAddressesByUserId(@PathVariable Long id) {
        return addressService.findAddressesByUserId(id);
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto) {
        return new ResponseEntity<>(addressService.create(addressDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        return new ResponseEntity<>(addressService.update(id, addressDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteById(id);
    }
}


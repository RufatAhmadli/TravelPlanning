package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.enums.AddressType;
import edu.az.example.web.travelplanning.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByCityIgnoreCase(String city);

    List<Address> findAllByAddressType(AddressType addressType);

    List<Address> findAllByUserId(Long userId);

    @Query("select a from Address a where upper(a.street) = upper(?1) and upper(a.streetNumber) = upper(?2)")
    List<Address> findAllByStreet(String street, String number);
}

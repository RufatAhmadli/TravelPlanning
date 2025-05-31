package edu.az.example.web.travelplanning.model.entity;

import edu.az.example.web.travelplanning.enums.AddressType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "Addresses"
)
public class Address {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(name = "№",nullable = false)
    private String streetNumber;

    @Column(nullable = false)
    private String postalCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType addressType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}

package edu.az.example.web.travelplanning.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Entity
@Table(
        name = "trips"
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String departure;
    @Column(nullable = false)
    private String destination;
    @Column(nullable = false)
    private LocalDate departureTime;
    @Column(nullable = false)
    private LocalDate arrivalTime;
    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "trips")
    private List<User> users;
}

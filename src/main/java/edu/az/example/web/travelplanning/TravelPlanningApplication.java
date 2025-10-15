package edu.az.example.web.travelplanning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class TravelPlanningApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelPlanningApplication.class, args);
    }

}

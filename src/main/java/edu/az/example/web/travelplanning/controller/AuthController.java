package edu.az.example.web.travelplanning.controller;

import edu.az.example.web.travelplanning.model.dto.AuthRequest;
import edu.az.example.web.travelplanning.model.dto.AuthResponse;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/authenticate")
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        return authService.register(userDto);
    }
}

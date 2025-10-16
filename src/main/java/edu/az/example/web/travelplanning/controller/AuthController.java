package edu.az.example.web.travelplanning.controller;

import edu.az.example.web.travelplanning.dto.AuthRequest;
import edu.az.example.web.travelplanning.dto.AuthResponse;
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
    private  final AuthService authService;

    @PostMapping("/authenticate")
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }
}

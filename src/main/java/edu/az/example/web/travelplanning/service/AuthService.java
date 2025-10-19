package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.dto.AuthRequest;
import edu.az.example.web.travelplanning.dto.AuthResponse;
import edu.az.example.web.travelplanning.dto.UserDto;
import edu.az.example.web.travelplanning.mapper.UserMapper;
import edu.az.example.web.travelplanning.model.entity.Role;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.repository.RoleRepository;
import edu.az.example.web.travelplanning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            return new AuthResponse(jwt, "Authenticated");
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    @Transactional
    public AuthResponse register(UserDto userDto) {
        validateRegistration(userDto);
        User user = completeRegistrationData(userDto);
        userRepository.save(user);

        String jwt = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password("")
                        .authorities(user.getRole().getName())
                        .build());

        return new AuthResponse(jwt, "Registration successful");
    }

    private void validateRegistration(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private User completeRegistrationData(UserDto userDto) {
        User userEntity = userMapper.toUserEntity(userDto);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        userEntity.setRole(userRole);
        return userEntity;
    }
}

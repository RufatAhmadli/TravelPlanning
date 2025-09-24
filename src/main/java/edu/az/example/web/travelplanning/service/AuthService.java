package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.exception.ConfirmPasswordException;
import edu.az.example.web.travelplanning.exception.EmailAlreadyExistsException;
import edu.az.example.web.travelplanning.mapper.UserMapper;
import edu.az.example.web.travelplanning.model.dto.AuthRequest;
import edu.az.example.web.travelplanning.model.dto.AuthResponse;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.model.entity.Role;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword());
            String jwt = getJwt(token);
            return new AuthResponse(jwt, "Authenticated");
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    public AuthResponse register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        User user = userMapper.toUser(userDto);
        if (userDto.getConfirmPassword().equals(userDto.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            user.setRole(new Role(2L, "USER"));
            userRepository.save(user);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDto.getEmail(), userDto.getPassword()
            );
            String jwt = getJwt(token);
            return new AuthResponse(jwt, "Registered");
        } else {
            throw new ConfirmPasswordException();
        }
    }

    private String getJwt(UsernamePasswordAuthenticationToken token) {
        Authentication authentication = authenticationManager.authenticate(token);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtService.generateToken(userDetails);
    }


}

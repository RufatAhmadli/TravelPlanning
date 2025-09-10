package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.model.dto.AuthRequest;
import edu.az.example.web.travelplanning.model.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse authenticate(AuthRequest authRequest) {
        try{
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            return new AuthResponse(jwt, "Authenticated");
        }catch(Exception ex){
            throw new IllegalStateException(ex.getMessage(), ex);
        }

    }
}

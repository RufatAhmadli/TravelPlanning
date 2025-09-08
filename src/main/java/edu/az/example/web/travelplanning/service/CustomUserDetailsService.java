package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().getName().toUpperCase())
//                .authorities(getAuthorities(user))
                .build();
    }

//    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
//        if (user.getRole() != null) {
//            String roleName = "ROLE_" + user.getRole().getName().toUpperCase();
//            return Collections.singletonList(new SimpleGrantedAuthority(roleName));
//        }
//
//        return Collections.emptyList();
//    }
}

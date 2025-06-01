package com.example.conduit.modules.user.service;

import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        User foundUser = user.get();
        return org.springframework.security.core.userdetails.User
                .withUsername(foundUser.getEmail())
                .password(foundUser.getPassword())
                .authorities("USER")
                .build();
    }
}

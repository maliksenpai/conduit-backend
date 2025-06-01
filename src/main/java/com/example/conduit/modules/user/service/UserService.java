package com.example.conduit.modules.user.service;

import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void createUser(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> loginUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            User foundUser = existingUser.get();
            if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                return Optional.of(foundUser);
            }
        }
        throw new BadCredentialsException("Invalid email or password");
    }

    public Optional<User> updateUser(User presentUser, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(presentUser.getId());
        if (existingUser.isPresent()) {
            if (updatedUser.getUsername() != null) {
                presentUser.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                Optional<User> userWithEmail = userRepository.findByEmail(updatedUser.getEmail());
                if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(presentUser.getId())) {
                    throw new BadCredentialsException("Email already exists");
                }
                presentUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                presentUser.setPassword(encodePassword(updatedUser.getPassword()));
            }
            if (updatedUser.getBio() != null) {
                presentUser.setBio(updatedUser.getBio());
            }
            if (updatedUser.getImage() != null) {
                presentUser.setImage(updatedUser.getImage());
            }
            userRepository.save(presentUser);
            return Optional.of(presentUser);
        } else {
            throw new BadCredentialsException("Invalid user");
        }
    }
}

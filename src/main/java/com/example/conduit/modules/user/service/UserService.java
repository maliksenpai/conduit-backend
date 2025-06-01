package com.example.conduit.modules.user.service;

import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

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
        return userRepository.findById(presentUser.getId())
                .map(existingUser -> {
                    updateFieldIfPresent(updatedUser.getUsername(), existingUser::setUsername);
                    updateEmailIfValid(updatedUser.getEmail(), existingUser);
                    updateFieldIfPresent(updatedUser.getPassword(),
                            pwd -> existingUser.setPassword(encodePassword(pwd)));
                    updateFieldIfPresent(updatedUser.getBio(), existingUser::setBio);
                    updateFieldIfPresent(updatedUser.getImage(), existingUser::setImage);

                    return userRepository.save(existingUser);
                });
    }

    private <T> void updateFieldIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private void updateEmailIfValid(String newEmail, User existingUser) {
        if (newEmail != null && !newEmail.equals(existingUser.getEmail())) {
            userRepository.findByEmail(newEmail).ifPresent(conflictUser -> {
                if (!conflictUser.getId().equals(existingUser.getId())) {
                    throw new BadCredentialsException("Email already exists");
                }
            });
            existingUser.setEmail(newEmail);
        }
    }
}

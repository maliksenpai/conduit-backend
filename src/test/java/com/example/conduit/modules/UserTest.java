package com.example.conduit.modules;

import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import com.example.conduit.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    private final User userTest = new User(7, "maliksenpai6", "malik6@hotmail.com", "123123", "", "", "");

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void shouldEqualPasswords() {
        String encodedTestPassword = userService.encodePassword(userTest.getPassword());
        assertTrue(passwordEncoder.matches(userTest.getPassword(), encodedTestPassword));
    }

    @Test
    void shouldCreateUser() {
        userService.createUser(userTest);
        verify(userRepository, times(1)).save(userTest);
    }

    @Test
    void shouldLoginUser() {
        User wrongUser = new User(7, "maliksenpai6", "malik6@hotmail.com", "345678", "", "", "");
        assertThrows(BadCredentialsException.class, () -> userService.loginUser(wrongUser));
        User storedUser = new User(7, "maliksenpai6", "malik6@hotmail.com", "123123", "", "", "");
        storedUser.setPassword(userService.encodePassword(storedUser.getPassword()));
        when(userRepository.findByEmail(userTest.getEmail())).thenReturn(Optional.of(storedUser));
        Optional<User> trueLoginAttempt = userService.loginUser(userTest);
        assertTrue(trueLoginAttempt.isPresent());
    }

    @Test
    void shouldUpdateUser() {
        User updatedUser = new User(7, "maliksenpai7", "malik6@hotmail.com", "123123", "", "", "");
        when(userRepository.findById(userTest.getId())).thenReturn(Optional.of(userTest));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Optional<User> returnedUser = userService.updateUser(userTest, updatedUser);
        assertTrue(returnedUser.isPresent());
        assertEquals(returnedUser.get().getUsername(), userTest.getUsername());
    }
}

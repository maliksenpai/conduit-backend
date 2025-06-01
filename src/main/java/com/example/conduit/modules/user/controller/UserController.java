package com.example.conduit.modules.user.controller;

import com.example.conduit.modules.user.dto.UserResponse;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import com.example.conduit.modules.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<UserResponse> getUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Optional<User> user = this.userRepository.findByEmail(userDetails.getUsername());
            if (user.isPresent()) {
                UserResponse userResponse = new UserResponse();
                userResponse.setUser(user.get());
                return new ResponseEntity<>(userResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Validated UserResponse userResponse) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Optional<User> user = this.userRepository.findByEmail(userDetails.getUsername());
            if (user.isPresent()) {
                User presentUser = user.get();
                User updatedUser = userResponse.getUser();
                if (updatedUser.getUsername() != null) {
                    presentUser.setUsername(updatedUser.getUsername());
                }
                if (updatedUser.getEmail() != null) {
                    presentUser.setEmail(updatedUser.getEmail());
                }
                userService.updateUser(presentUser);
                userResponse.setUser(user.get());
                return new ResponseEntity<>(userResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

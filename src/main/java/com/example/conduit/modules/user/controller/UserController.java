package com.example.conduit.modules.user.controller;

import com.example.conduit.modules.user.dto.UserResponse;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import com.example.conduit.modules.user.service.UserService;
import com.example.conduit.shared.dto.ErrorObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "User")
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
                Optional<User> newUser = userService.updateUser(presentUser, updatedUser);
                if (newUser.isPresent()) {
                    userResponse.setUser(user.get());
                    return new ResponseEntity<>(userResponse, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            UserResponse response = new UserResponse();
            ErrorObject errorObject = new ErrorObject();
            errorObject.setBody(List.of(e.getMessage()));
            response.setErrors(errorObject);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.example.conduit.modules.user.controller;

import com.example.conduit.modules.user.dto.UserResponse;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.service.UserService;
import com.example.conduit.security.JwtUtil;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UsersController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Validated UserResponse userResponse) {
        try {
            User user = userResponse.getUser();
            userService.createUser(user);
            jwtUtil.addUserToToken(user);
            userResponse.setUser(user);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody @Validated UserResponse userResponse) {
        try {
            User user = userResponse.getUser();
            Optional<User> loggedInUser = userService.loginUser(user);
            if (loggedInUser.isPresent()) {
                User presentUser = loggedInUser.get();
                jwtUtil.addUserToToken(presentUser);
                userResponse.setUser(presentUser);
                return ResponseEntity.ok(userResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(null);
        }
        return null;
    }
}

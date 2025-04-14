package com.example.conduit.modules.user.controller;

import com.example.conduit.modules.user.dto.UserResponse;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.service.UserService;
import com.example.conduit.security.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final JwtUtil jwtUtil = new JwtUtil();

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public UserResponse createUser(@RequestBody UserResponse userResponse){
        User user = userResponse.getUser();
        jwtUtil.addUserToToken(user);
        userService.createUser(user);
        return userResponse;
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody UserResponse userResponse){
        User user = userResponse.getUser();
        User loggedInUser = userService.loginUser(user);
        if (loggedInUser != null) {
            jwtUtil.addUserToToken(loggedInUser);
            userResponse.setUser(loggedInUser);
            return userResponse;
        }
        return null;
    }
}

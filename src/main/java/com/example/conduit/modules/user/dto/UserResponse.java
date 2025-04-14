package com.example.conduit.modules.user.dto;

import com.example.conduit.modules.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private User user;
}


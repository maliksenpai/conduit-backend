package com.example.conduit.modules.user.dto;

import com.example.conduit.modules.user.model.User;
import com.example.conduit.shared.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse extends BaseDTO {
    private User user;
}


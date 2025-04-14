package com.example.conduit.modules.user.repository;

import com.example.conduit.modules.user.model.User;
import com.example.conduit.shared.GenericRepository;

public interface UserRepository extends GenericRepository<User, Long> {
    User findByEmail(String email);
}

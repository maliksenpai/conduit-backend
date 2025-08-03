package com.example.conduit.modules.user.repository;

import com.example.conduit.modules.user.model.User;
import com.example.conduit.shared.GenericRepository;

import java.util.Optional;

public interface  UserRepository extends GenericRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}

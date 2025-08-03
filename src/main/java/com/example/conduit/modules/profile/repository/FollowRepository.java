package com.example.conduit.modules.profile.repository;

import com.example.conduit.modules.profile.model.FollowRelation;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.shared.GenericRepository;

import java.util.Optional;

public interface FollowRepository extends GenericRepository<FollowRelation, Long> {
    boolean existsByFollowingUser_IdAndFollowedUser_Id(Long followingUser_id, Long followedUser_id);
    Optional<FollowRelation> findByFollowingUserAndFollowedUser(User followingUser, User followedUser);
}

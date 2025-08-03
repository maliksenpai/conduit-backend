package com.example.conduit.modules.profile.repository;

import com.example.conduit.modules.profile.model.FollowRelation;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.shared.GenericRepository;

import java.util.Optional;

public interface FollowRepository extends GenericRepository<FollowRelation, Integer> {
    boolean existsByFollowingUser_IdAndFollowedUser_Id(Integer followingUserId, Integer followedUserId);
    Optional<FollowRelation> findByFollowingUserAndFollowedUser(User followingUser, User followedUser);
}

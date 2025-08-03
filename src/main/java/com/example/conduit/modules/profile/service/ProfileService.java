package com.example.conduit.modules.profile.service;

import com.example.conduit.mapper.ArticleMapper;
import com.example.conduit.modules.profile.model.FollowRelation;
import com.example.conduit.modules.profile.model.Profile;
import com.example.conduit.modules.profile.repository.FollowRepository;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final FollowRepository followRepository;

    public ProfileService(UserRepository userRepository, ArticleMapper articleMapper, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.articleMapper = articleMapper;
        this.followRepository = followRepository;
    }

    public Profile getProfileWithUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return articleMapper.userToProfile(user.get());
        } else {
            throw new IllegalArgumentException("Profile not found with username: " + username);
        }
    }

    @Transactional
    public Profile followProfileWithUsername(String username) {
        Optional<User> followedUser = userRepository.findByUsername(username);
        if (followedUser.isEmpty()) {
            throw new IllegalArgumentException("Profile not found with username: " + username);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Optional<User> user = this.userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            throw new BadCredentialsException("Unauthorized to follow this user");
        }
        boolean isAlreadyFollowed = followRepository.existsByFollowingUser_IdAndFollowedUser_Id(user.get().getId(), followedUser.get().getId());
        if (isAlreadyFollowed) {
            throw new IllegalArgumentException("This user already following that user");
        }
        FollowRelation followRelation = new FollowRelation();
        followRelation.setFollowedUser(followedUser.get());
        followRelation.setFollowingUser(user.get());
        followRepository.save(followRelation);
        return articleMapper.userToProfile(followedUser.get());
    }

    @Transactional
    public Profile unfollowUser(String username) {
        Optional<User> followedUser = userRepository.findByUsername(username);
        if (followedUser.isEmpty()) {
            throw new IllegalArgumentException("Profile not found with username: " + username);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Optional<User> user = this.userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            throw new BadCredentialsException("Unauthorized to follow this user");
        }
        Optional<FollowRelation> followRelation = followRepository.findByFollowingUserAndFollowedUser(user.get(), followedUser.get());
        if (followRelation.isEmpty()) {
            throw new IllegalArgumentException("This user already not following that user");
        }
        followRepository.delete(followRelation.get());
        return articleMapper.userToProfile(followedUser.get());
    }
}

package com.example.conduit.mapper;

import com.example.conduit.modules.article.model.Article;
import com.example.conduit.modules.article.model.ArticleData;
import com.example.conduit.modules.profile.model.Profile;
import com.example.conduit.modules.profile.repository.FollowRepository;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ArticleMapper {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public ArticleMapper(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public ArticleData articleToArticleData(Article article) {
        return ArticleData.builder()
                .id(article.getId())
                .body(article.getBody())
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .tagList(article.getTagList())
                .createdAt(article.getCreatedAt())
                .favoritesCount(article.getFavoritesCount())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    public Article articleDataToArticle(ArticleData articleData) {
        User user = articleData.getAuthor();
        Profile profile = Profile.builder()
                .image(user.getImage())
                .username(user.getUsername())
                .following(false)
                .bio(user.getBio())
                .build();
        return Article.builder()
                .id(articleData.getId())
                .body(articleData.getBody())
                .slug(articleData.getSlug())
                .title(articleData.getTitle())
                .description(articleData.getDescription())
                .tagList(articleData.getTagList())
                .createdAt(articleData.getCreatedAt())
                .favoritesCount(articleData.getFavoritesCount())
                .updatedAt(articleData.getUpdatedAt())
                .author(profile)
                .build();
    }

    public Profile userToProfile(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Optional<User> currentUser = userRepository.findByEmail(userDetails.getUsername());
        boolean isFollowing = followRepository.existsByFollowingUser_IdAndFollowedUser_Id(currentUser.get().getId(), user.getId());
        return Profile
                .builder()
                .username(user.getUsername())
                .image(user.getImage())
                .bio(user.getBio())
                .following(isFollowing)
                .build();
    }

}

package com.example.conduit.mapper;

import com.example.conduit.modules.article.model.ArticleData;
import com.example.conduit.modules.article.model.Comment;
import com.example.conduit.modules.article.model.CommentData;
import com.example.conduit.modules.profile.model.Profile;
import com.example.conduit.modules.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentData commentToCommentData(Comment comment, ArticleData articleData) {
        return CommentData.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .article(articleData)
                .build();
    }

    public Comment commentDataToComment(CommentData commentData) {
        User user = commentData.getAuthor();
        Profile profile = Profile.builder()
                .image(user.getImage())
                .username(user.getUsername())
                .following(false)
                .bio(user.getBio())
                .build();
        return Comment.builder()
                .id(commentData.getId())
                .body(commentData.getBody())
                .createdAt(commentData.getCreatedAt())
                .updatedAt(commentData.getUpdatedAt())
                .author(profile)
                .build();
    }
}

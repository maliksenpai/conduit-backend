package com.example.conduit.modules.article.service;

import com.example.conduit.mapper.ArticleMapper;
import com.example.conduit.mapper.CommentMapper;
import com.example.conduit.modules.article.model.ArticleData;
import com.example.conduit.modules.article.model.Comment;
import com.example.conduit.modules.article.model.CommentData;
import com.example.conduit.modules.article.repository.ArticleRepository;
import com.example.conduit.modules.article.repository.CommentRepository;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, UserRepository userRepository, ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @CacheEvict(value = "comments", key = "#slug")
    public Comment addComment(String slug, Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Optional<User> optionalUser = this.userRepository.findByEmail(userDetails.getUsername());
        if (optionalUser.isEmpty()) {
            throw new BadCredentialsException("Invalid user");
        }
        Optional<ArticleData> optionalArticleData = articleRepository.findBySlug(slug);
        if (optionalArticleData.isEmpty()) {
            throw new BadCredentialsException("Invalid article");
        }
        CommentData commentData = commentMapper.commentToCommentData(comment, optionalArticleData.get());
        commentData.setAuthor(optionalUser.get());
        Comment responseComment = commentMapper.commentDataToComment(commentRepository.save(commentData));
        responseComment.setAuthor(articleMapper.userToProfile(optionalUser.get()));
        return responseComment;
    }

    @CacheEvict(value = "comments", key = "#slug")
    public boolean deleteComment(String slug, String commentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        Optional<CommentData> currentComment = commentRepository.findById(Integer.parseInt(commentId));
        Optional<ArticleData> currentArticle = articleRepository.findBySlug(slug);
        if (currentArticle.isEmpty()) {
            throw new IllegalArgumentException("Article not found with slug: " + slug);
        }
        if (currentComment.isEmpty()) {
            throw new IllegalArgumentException("Comment not found with id: " + commentId);
        }

        if (user.isEmpty() || !currentComment.get().getAuthor().getUsername().equals(user.get().getUsername())) {
            throw new BadCredentialsException("Unauthorized to delete this comment");
        }
        try {
            commentRepository.delete(currentComment.get());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Cacheable(value = "comments", key = "#slug")
    public List<Comment> getListCommentOfArticle(String slug) {
        Optional<ArticleData> currentArticle = articleRepository.findBySlug(slug);
        if (currentArticle.isEmpty()) {
            throw new IllegalArgumentException("Article not found with slug: " + slug);
        }
        Optional<List<CommentData>> commentDataList = commentRepository.getAllByArticle_Slug(slug);
        return commentDataList.map(commentData -> commentData.stream().map(commentMapper::commentDataToComment).toList()).orElse(Collections.emptyList());
    }
}

package com.example.conduit.modules.article.service;

import com.example.conduit.mapper.ArticleMapper;
import com.example.conduit.modules.article.dto.ArticleDTO;
import com.example.conduit.modules.article.model.Article;
import com.example.conduit.modules.article.model.ArticleData;
import com.example.conduit.modules.article.repository.ArticleRepository;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import com.example.conduit.shared.utils.ObjectUtils;
import com.example.conduit.shared.utils.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleMapper = articleMapper;
    }

    @Cacheable(value = "articles")
    public List<Article> getArticleList(Pageable pageable) {
        return this.articleRepository.findAll(pageable).stream().map(articleMapper::articleDataToArticle).toList();
    }

    @CacheEvict(value = "articles", allEntries = true)
    public ArticleDTO createArticle(Article article) {
        try {
            ArticleDTO articleDTO = new ArticleDTO();
            article.setSlug(StringUtils.getSlugTitle(article.getTitle()));
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Optional<User> optionalUser = this.userRepository.findByEmail(userDetails.getUsername());
            if (optionalUser.isEmpty()) {
                throw new BadCredentialsException("Invalid user");
            }
            ArticleData articleData = articleMapper.articleToArticleData(article);
            articleData.setAuthor(optionalUser.get());
            Article responseArticle =  articleMapper.articleDataToArticle(articleRepository.save(articleData));
            responseArticle.setAuthor(articleMapper.userToProfile(optionalUser.get()));
            articleDTO.setArticle(responseArticle);
            return articleDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @CacheEvict(value = "articles", allEntries = true)
    public ArticleDTO updateArticle(Article updatedArticle, String slug) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Optional<User> user = this.userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty() || !updatedArticle.getAuthor().getUsername().equals(user.get().getUsername())) {
            throw new BadCredentialsException("Unauthorized to update this article");
        }

        Optional<ArticleData> currentArticle = this.articleRepository.findBySlug(slug);
        if (currentArticle.isPresent()) {
            ArticleData presentArticle = currentArticle.get();
            ObjectUtils.updateFieldIfPresent(updatedArticle.getTitle(), presentArticle::setTitle);
            if (updatedArticle.getTitle() != null) {
                presentArticle.setSlug(StringUtils.getSlugTitle(updatedArticle.getTitle()));
            }
            ObjectUtils.updateFieldIfPresent(updatedArticle.getBody(), presentArticle::setBody);
            ObjectUtils.updateFieldIfPresent(updatedArticle.getDescription(), presentArticle::setDescription);
            this.articleRepository.save(presentArticle);
            return new ArticleDTO(articleMapper.articleDataToArticle(presentArticle));
        } else {
            throw new IllegalArgumentException("Article not found with slug: " + slug);
        }
    }

    @CacheEvict(value = "articles", allEntries = true)
    public boolean deleteArticle(String slug) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Optional<User> user = this.userRepository.findByEmail(userDetails.getUsername());
        Optional<ArticleData> currentArticle = this.articleRepository.findBySlug(slug);

        if (currentArticle.isEmpty()) {
            throw new IllegalArgumentException("Article not found with slug: " + slug);
        }

        if (user.isEmpty() || !currentArticle.get().getAuthor().getUsername().equals(user.get().getUsername())) {
            throw new BadCredentialsException("Unauthorized to delete this article");
        }
        try {
            this.articleRepository.delete(currentArticle.get());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

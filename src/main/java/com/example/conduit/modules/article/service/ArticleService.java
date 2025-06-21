package com.example.conduit.modules.article.service;

import com.example.conduit.modules.article.dto.ArticleDTO;
import com.example.conduit.modules.article.model.Article;
import com.example.conduit.modules.article.model.Author;
import com.example.conduit.modules.article.repository.ArticleRepository;
import com.example.conduit.modules.user.model.User;
import com.example.conduit.modules.user.repository.UserRepository;
import com.example.conduit.shared.utils.ObjectUtils;
import com.example.conduit.shared.utils.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<Article> getArticleList() {
        return this.articleRepository.findAll();
    }

    public ArticleDTO createArticle(Article article) {
        try {
            ArticleDTO articleDTO = new ArticleDTO();
            article.setCreatedAt(Instant.now());
            article.setUpdatedAt(Instant.now());
            article.setFavoritesCount(0);
            article.setSlug(StringUtils.getSlugTitle(article.getTitle()));
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Optional<User> user = this.userRepository.findByEmail(userDetails.getUsername());
            if (user.isPresent()) {
                User userPresent = user.get();
                Author author = Author
                        .builder()
                        .username(userPresent.getUsername())
                        .image(userPresent.getImage())
                        .bio(userPresent.getBio())
                        .following(false)
                        .build();
                article.setAuthor(author);
            } else {
                throw new BadCredentialsException("Invalid user");
            }
            articleRepository.save(article);
            articleDTO.setArticle(article);
            return articleDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArticleDTO updateArticle(Article updatedArticle, String slug) {
        Optional<Article> currentArticle = this.articleRepository.findBySlug(slug);
        if (currentArticle.isPresent()) {
            Article presentArticle = currentArticle.get();
            ObjectUtils.updateFieldIfPresent(updatedArticle.getTitle(), presentArticle::setTitle);
            if (updatedArticle.getTitle() != null) {
                presentArticle.setSlug(StringUtils.getSlugTitle(updatedArticle.getTitle()));
            }
            ObjectUtils.updateFieldIfPresent(updatedArticle.getBody(), presentArticle::setBody);
            ObjectUtils.updateFieldIfPresent(updatedArticle.getDescription(), presentArticle::setDescription);
            return new ArticleDTO(this.articleRepository.save(presentArticle));
        } else {
            throw new BadCredentialsException("Invalid slug");
        }
    }
}

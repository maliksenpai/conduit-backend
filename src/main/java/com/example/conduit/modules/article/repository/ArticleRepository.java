package com.example.conduit.modules.article.repository;

import com.example.conduit.modules.article.model.Article;
import com.example.conduit.shared.GenericRepository;

import java.util.Optional;

public interface ArticleRepository  extends GenericRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);
}

package com.example.conduit.modules.article.repository;

import com.example.conduit.modules.article.model.ArticleData;
import com.example.conduit.shared.GenericRepository;

import java.util.Optional;

public interface ArticleRepository  extends GenericRepository<ArticleData, Long> {
    Optional<ArticleData> findBySlug(String slug);
}

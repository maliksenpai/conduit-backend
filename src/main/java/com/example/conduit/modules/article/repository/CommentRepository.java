package com.example.conduit.modules.article.repository;

import com.example.conduit.modules.article.model.CommentData;
import com.example.conduit.shared.GenericRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends GenericRepository<CommentData, Long> {
    Optional<List<CommentData>> getAllByArticle_Slug(String articleSlug);
}

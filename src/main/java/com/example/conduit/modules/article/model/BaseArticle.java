package com.example.conduit.modules.article.model;

import com.example.conduit.modules.article.service.ArticleService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String slug;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Title cannot be empty", groups = ArticleService.class)
    private String title;
    @NotBlank(message = "Description cannot be empty", groups = ArticleService.class)
    private String description;
    @NotBlank(message = "Body cannot be empty", groups = ArticleService.class)
    private String body;
    @ElementCollection
    private Set<String> tagList;
    private Instant createdAt;
    private Instant updatedAt;
    @Transient
    private Boolean favorited;
    private Integer favoritesCount;
}

package com.example.conduit.modules.article.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Table(name = "articles")
@Entity
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String slug;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    @NotBlank(message = "Body cannot be empty")
    private String body;
    @ElementCollection
    private Set<String> tagList;
    private Instant createdAt;
    private Instant updatedAt;
    @Transient
    private Boolean favorited;
    private Number favoritesCount;
    @Embedded
    private Author author;
}

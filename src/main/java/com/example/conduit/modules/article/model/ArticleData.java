package com.example.conduit.modules.article.model;

import com.example.conduit.modules.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Table(name = "articles")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleData extends BaseArticle {
    @ManyToOne()
    @JoinColumn(name = "author_id")
    private User author;

    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
        this.setFavoritesCount(0);
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(Instant.now());
    }
}

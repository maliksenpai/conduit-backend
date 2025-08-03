package com.example.conduit.modules.article.model;

import com.example.conduit.modules.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Table(name = "comments")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentData extends BaseComment {
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleData article;

    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(Instant.now());
    }
}

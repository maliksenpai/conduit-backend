package com.example.conduit.modules.article.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {
    private String username;
    private String bio;
    private String image;
    private Boolean following;
}

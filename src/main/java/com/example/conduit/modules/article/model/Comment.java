package com.example.conduit.modules.article.model;

import com.example.conduit.modules.profile.model.Profile;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseComment {
    @Embedded
    private Profile author;
}

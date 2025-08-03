package com.example.conduit.modules.profile.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    private String username;
    private String bio;
    private String image;
    private Boolean following;
}

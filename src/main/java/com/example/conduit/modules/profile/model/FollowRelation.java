package com.example.conduit.modules.profile.model;

import com.example.conduit.modules.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follow_relation", uniqueConstraints = @UniqueConstraint(columnNames = {"following_user_id", "followed_user_id"}))
public class FollowRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following_user_id", nullable = false)
    private User followingUser;

    @ManyToOne
    @JoinColumn(name = "followed_user_id", nullable = false)
    private User followedUser;
}

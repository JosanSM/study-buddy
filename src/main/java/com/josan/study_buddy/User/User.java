package com.josan.study_buddy.User;

import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Topic.Topic;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    private String name;
    private String email;
    private String user_tier;
    private LocalDateTime last_updated; //TODO: check naming convention for this (camelcase vs snake)

    @OneToMany(mappedBy = "user")
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Topic> topics = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        last_updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        last_updated = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", user_tier='" + user_tier + '\'' +
                ", last_updated=" + last_updated +
                '}';
    }
}

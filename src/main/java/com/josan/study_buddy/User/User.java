package com.josan.study_buddy.User;

import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Topic.Topic;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    private String name;
    private String email;
    private String user_tier;
    private LocalDateTime last_updated; //TODO: check naming convention for this (camelcase vs snake)

    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Topic> topics = new HashSet<>();

    // --- UserDetails contract ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) return List.of();
        return List.of(role.toGrantedAuthority());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    // Spring Security uses this as the principal identifier
    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // ---

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
                ", role=" + role +
                ", last_updated=" + last_updated +
                '}';
    }
}

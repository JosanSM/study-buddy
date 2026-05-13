package com.josan.study_buddy.User;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    private String name;
    private String email;
    private String user_tier;
    private LocalDateTime last_updated;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_tier() {
        return user_tier;
    }

    public void setUser_tier(String user_tier) {
        this.user_tier = user_tier;
    }

    public LocalDateTime getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(LocalDateTime last_updated) {
        this.last_updated = last_updated;
    }
}

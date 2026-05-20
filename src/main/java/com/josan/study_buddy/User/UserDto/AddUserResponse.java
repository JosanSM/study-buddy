package com.josan.study_buddy.User.UserDto;

import com.josan.study_buddy.User.User;

import java.time.LocalDateTime;

public class AddUserResponse {
    private Long id;
    private String name;
    private String email;
    private String userTier;
    private LocalDateTime lastUpdated;

    // from method creates a User Rsponse object out of a User object
    // so that we only expose what we define in this class in the response
    public static AddUserResponse from(User user){
        AddUserResponse responseObject = new AddUserResponse();
        responseObject.setId(user.getId());
        responseObject.setName(user.getName());
        responseObject.setEmail(user.getEmail());
        responseObject.setUserTier(user.getUser_tier());
        responseObject.setLastUpdated(user.getLast_updated());

        return responseObject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserTier() {
        return userTier;
    }

    public void setUserTier(String userTier) {
        this.userTier = userTier;
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

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userTier='" + userTier + '\'' +
                '}';
    }
}

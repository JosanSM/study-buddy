package com.josan.study_buddy.User.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddUserRequest {
    @NotBlank
    @Size(min=2, max=60)
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min=2, max=10)
    private String userTier;


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

    @Override
    public String toString() {
        return "UserRequest{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userTier='" + userTier + '\'' +
                '}';
    }
}

package com.josan.study_buddy.User.UserDto;

import com.josan.study_buddy.User.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    @NotNull
    Long id;
    @NotBlank
    @Size(min=2, max=60)
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min=2, max=10)
    private String userTier;

    public static UpdateUserRequest from (User user ) {
        return new UpdateUserRequest(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUser_tier()
        );
    }

    public UpdateUserRequest(Long id, String name, String email, String userTier) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userTier = userTier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getUserTier() {
        return userTier;
    }

    public void setUserTier(String userTier) {
        this.userTier = userTier;
    }
}

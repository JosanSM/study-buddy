package com.josan.study_buddy.User.UserDto;

import com.josan.study_buddy.User.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GenericUserResponse {
    @NotNull
    private Long id;
    @NotNull
    @Size(min=2, max=50)
    private String name;

    public GenericUserResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static GenericUserResponse from (User user) {
        return new GenericUserResponse(
                user.getId(),
                user.getName()
        );
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

    @Override
    public String toString() {
        return "GenericUserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

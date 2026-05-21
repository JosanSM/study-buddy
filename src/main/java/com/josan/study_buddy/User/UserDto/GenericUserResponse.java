package com.josan.study_buddy.User.UserDto;

import com.josan.study_buddy.User.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GenericUserResponse {
    @NotNull
    private Long id;
    @NotNull
    @Size(min=2, max=50)
    private String name;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String user_tier;

    public static GenericUserResponse from (User user) {
        return new GenericUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUser_tier()
        );
    }
}

package com.josan.study_buddy.User.UserDto;

import com.josan.study_buddy.User.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
}

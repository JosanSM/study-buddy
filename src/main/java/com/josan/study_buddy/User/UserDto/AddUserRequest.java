package com.josan.study_buddy.User.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
}

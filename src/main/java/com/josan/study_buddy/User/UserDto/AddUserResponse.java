package com.josan.study_buddy.User.UserDto;

import com.josan.study_buddy.User.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
}

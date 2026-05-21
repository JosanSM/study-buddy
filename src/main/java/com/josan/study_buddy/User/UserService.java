package com.josan.study_buddy.User;

import com.josan.study_buddy.User.UserDto.AddUserRequest;
import com.josan.study_buddy.User.UserDto.GenericUserResponse;
import com.josan.study_buddy.User.UserDto.UpdateUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<GenericUserResponse> findAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> GenericUserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .toList();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    public User updateUser(UpdateUserRequest request) {

        User existingUser = this.findUserById(request.getId()).orElseThrow();
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setUser_tier(request.getUserTier()); // TODO: move this eventually to its own method to avoid users manipulating it
        try {
            return this.saveUser(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update the user");
        }
    }

    public User buildUser(AddUserRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setUser_tier(userRequest.getUserTier());
        user.setName(userRequest.getName());

        return user;
    }
}

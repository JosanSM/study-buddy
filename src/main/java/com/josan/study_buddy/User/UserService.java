package com.josan.study_buddy.User;

import com.josan.study_buddy.User.UserDto.AddUserRequest;
import com.josan.study_buddy.User.UserDto.AddUserResponse;
import com.josan.study_buddy.User.UserDto.GenericUserResponse;
import com.josan.study_buddy.User.UserDto.UpdateUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .map(GenericUserResponse::from)
                .toList();
    }

    public Optional<GenericUserResponse> findUserById(Long id) {
        return userRepository.findById(id).map(GenericUserResponse::from);
    }

    @Transactional
    public AddUserResponse createUser(AddUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUser_tier(request.getUserTier());
        return AddUserResponse.from(userRepository.save(user));
    }

    @Transactional
    public GenericUserResponse updateUser(UpdateUserRequest request) {
        User existingUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getId()));
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setUser_tier(request.getUserTier()); // TODO: protect user_tier from direct update
        return GenericUserResponse.from(userRepository.save(existingUser));
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}
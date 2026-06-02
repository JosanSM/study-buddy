package com.josan.study_buddy.User;

import com.josan.study_buddy.User.UserDto.AddUserRequest;
import com.josan.study_buddy.User.UserDto.AddUserResponse;
import com.josan.study_buddy.User.UserDto.GenericUserResponse;
import com.josan.study_buddy.User.UserDto.UpdateUserRequest;
import com.josan.study_buddy.exception.DuplicateEmailException;
import com.josan.study_buddy.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public GenericUserResponse findUserById(Long id) {
        return userRepository.findById(id)
                .map(GenericUserResponse::from)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public AddUserResponse createUser(AddUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUser_tier(request.getUserTier());
        return AddUserResponse.from(userRepository.save(user));
    }

    @Transactional
    public GenericUserResponse updateUser(UpdateUserRequest request) {
        User existingUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException(request.getId()));
        if (userRepository.existsByEmailAndIdNot(request.getEmail(), request.getId())) {
            throw new DuplicateEmailException();
        }
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setUser_tier(request.getUserTier());
        return GenericUserResponse.from(userRepository.save(existingUser));
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}

package com.josan.study_buddy.Service;

import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserRepository;
import com.josan.study_buddy.User.UserService;
import com.josan.study_buddy.User.UserDto.AddUserRequest;
import com.josan.study_buddy.User.UserDto.AddUserResponse;
import com.josan.study_buddy.User.UserDto.GenericUserResponse;
import com.josan.study_buddy.User.UserDto.UpdateUserRequest;
import com.josan.study_buddy.exception.DuplicateEmailException;
import com.josan.study_buddy.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User buildUser(long id) {
        return User.builder()
                .id(id)
                .name("Test User")
                .email("test@example.com")
                .user_tier("pro")
                .build();
    }

    // findUserById

    @Test
    public void findUserById_UserExists_ReturnsGenericUserResponse() {
        User user = buildUser(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        GenericUserResponse response = userService.findUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test User", response.getName());
    }

    @Test
    public void findUserById_UserNotFound_ThrowsUserNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(99L));
    }

    // createUser

    @Test
    public void createUser_UniqueEmail_ReturnsAddUserResponse() {
        AddUserRequest request = AddUserRequest.builder()
                .name("New User")
                .email("new@example.com")
                .userTier("pro")
                .build();
        User saved = buildUser(1L);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        AddUserResponse response = userService.createUser(request);

        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void createUser_DuplicateEmail_ThrowsDuplicateEmailException() {
        AddUserRequest request = AddUserRequest.builder()
                .name("New User")
                .email("duplicate@example.com")
                .userTier("pro")
                .build();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any());
    }

    // updateUser

    @Test
    public void updateUser_UserExists_UniqueEmail_ReturnsUpdatedResponse() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .name("Updated")
                .email("updated@example.com")
                .userTier("pro")
                .build();
        User existing = buildUser(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmailAndIdNot("updated@example.com", 1L)).thenReturn(false);
        when(userRepository.save(existing)).thenReturn(existing);

        GenericUserResponse response = userService.updateUser(request);

        assertNotNull(response);
        verify(userRepository).save(existing);
    }

    @Test
    public void updateUser_UserNotFound_ThrowsUserNotFoundException() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(99L)
                .name("X")
                .email("x@example.com")
                .userTier("pro")
                .build();
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(request));
    }

    @Test
    public void updateUser_DuplicateEmail_ThrowsDuplicateEmailException() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .name("User")
                .email("taken@example.com")
                .userTier("pro")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(buildUser(1L)));
        when(userRepository.existsByEmailAndIdNot("taken@example.com", 1L)).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.updateUser(request));
        verify(userRepository, never()).save(any());
    }

    // deleteUserById

    @Test
    public void deleteUserById_UserExists_DeletesUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUserById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void deleteUserById_UserNotFound_ThrowsUserNotFoundException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(99L));
        verify(userRepository, never()).deleteById(any());
    }
}

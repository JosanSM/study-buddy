package com.josan.study_buddy.User;

import com.josan.study_buddy.User.UserDto.AddUserRequest;
import com.josan.study_buddy.User.UserDto.AddUserResponse;
import com.josan.study_buddy.User.UserDto.GenericUserResponse;
import com.josan.study_buddy.User.UserDto.UpdateUserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<GenericUserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericUserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping("/")
    public ResponseEntity<AddUserResponse> addUser(@Valid @RequestBody AddUserRequest userRequest) {
        AddUserResponse created = userService.createUser(userRequest);
        URI location = URI.create("users/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/")
    public ResponseEntity<GenericUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}

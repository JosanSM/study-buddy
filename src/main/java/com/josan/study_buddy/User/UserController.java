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

    UserService userService;

    UserController(UserService userservice) {
        this.userService = userservice;
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        System.out.println("DEBUG");
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<AddUserResponse> addUser(@Valid @RequestBody AddUserRequest userRequest) {
        // TODO: Need to validate if theres a user with a given email
        User user = userService.buildUser(userRequest);
        userService.saveUser(user);

        URI location = URI.create("users/" + user.getId());

        return ResponseEntity
                .created(location)
                .body(AddUserResponse.from(user));
    }


    /*
        TODO: we need to move buisness logic from controller to service layer. getting user, setting new user attributes, and saving should be 
        done in the service layer.
    */
    @PutMapping("/")
    public ResponseEntity<UpdateUserRequest> updateUser(@Valid @RequestBody UpdateUserRequest request) { // TODO: clarify whether this type a methods need some type of additional layer fromm the front end to avoid manipulation through dev tools
        if(!userService.userExists(request.getId())) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = userService.findUserById(request.getId()).orElseThrow();
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setUser_tier(request.getUserTier()); // TODO: move this eventually to its own method to avoid users manipulating it

        try {
            userService.saveUser(existingUser);
            return ResponseEntity.ok(UpdateUserRequest.from(existingUser));
        } catch (RuntimeException e) {
          return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericUserResponse> deleteById(@PathVariable Long id) {
        if(!userService.userExists(id)) {
            return ResponseEntity.notFound().build();
        }

        // online I found that it is better to return no content, but this concerns me because it doesn't really confirm if the entity was deleted
        // I could modify the deleteById method to return a boolean and build validation logic around that. Is that ideal?

        /*
        TOOD: for above question, if it failed to delete we would throw errors and catch them and throw the approrpiate response code.
        */
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}

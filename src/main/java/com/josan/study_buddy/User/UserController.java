package com.josan.study_buddy.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

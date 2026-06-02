package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends StudyBuddyException {

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}

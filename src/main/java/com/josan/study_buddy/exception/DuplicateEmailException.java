package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends StudyBuddyException {

    public DuplicateEmailException() {
        super("A user with that email already exists", HttpStatus.CONFLICT);
    }
}

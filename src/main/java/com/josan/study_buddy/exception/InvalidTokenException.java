package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends StudyBuddyException {

    public InvalidTokenException() {
        super("Invalid or expired token", HttpStatus.UNAUTHORIZED);
    }
}

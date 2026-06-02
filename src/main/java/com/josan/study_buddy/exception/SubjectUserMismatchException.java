package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public class SubjectUserMismatchException extends StudyBuddyException {

    public SubjectUserMismatchException() {
        super("Subject does not belong to the specified user", HttpStatus.BAD_REQUEST);
    }
}

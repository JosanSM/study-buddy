package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public class DuplicateSubjectNameException extends StudyBuddyException {

    public DuplicateSubjectNameException() {
        super("You already have a subject with that name", HttpStatus.CONFLICT);
    }
}

package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public class SubjectNotEmptyException extends StudyBuddyException {

    public SubjectNotEmptyException() {
        super("Cannot delete a subject that still has topics", HttpStatus.CONFLICT);
    }
}

package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public class SubjectNotFoundException extends StudyBuddyException {

    public SubjectNotFoundException(Long id) {
        super("Subject not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}

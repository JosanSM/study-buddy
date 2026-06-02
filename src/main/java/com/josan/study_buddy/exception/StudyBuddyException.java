package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public abstract class StudyBuddyException extends RuntimeException {

    private final HttpStatus status;

    protected StudyBuddyException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

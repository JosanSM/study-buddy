package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public class TopicNotFoundException extends StudyBuddyException {

    public TopicNotFoundException(Long id) {
        super("Topic not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}

package com.josan.study_buddy.exception;

import org.springframework.http.HttpStatus;

public class DuplicateTopicTitleException extends StudyBuddyException {

    public DuplicateTopicTitleException() {
        super("A topic with that title already exists in this subject", HttpStatus.CONFLICT);
    }
}

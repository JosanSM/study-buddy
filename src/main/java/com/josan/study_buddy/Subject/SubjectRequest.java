package com.josan.study_buddy.Subject;


public class SubjectRequest {
    private Long userId;
    private String name;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

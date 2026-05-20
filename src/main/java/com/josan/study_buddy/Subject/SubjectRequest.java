package com.josan.study_buddy.Subject;


/*
TODO: Considder adding lombok to your project so we can use
@Getter and @Setter and @Builder annotations.

That way we simplify this Class object to the following (this goes for all DTO objects you make):

@Getter
@Setter
@Builder
public class SubjectRequest {
    private Long userId;
    private String name;
}

*/
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

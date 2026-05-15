package com.josan.study_buddy.Subject;

import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    private final SubjectService subjectService;
    private final UserService userService;

    public SubjectController(SubjectService subjectService, UserService userService)  {
        this.subjectService = subjectService;
        this.userService = userService;
    }

    @GetMapping("/")
    public List<Subject> getAllSubjects() {
        return subjectService.findAllSubjects();
    }

    @PostMapping("/")
    public Subject addSubject(@RequestBody SubjectRequest request) {
        Subject subject = new Subject();
        subject = subjectService.buildSubject(request);
        return subjectService.saveSubject(subject);
    }
}

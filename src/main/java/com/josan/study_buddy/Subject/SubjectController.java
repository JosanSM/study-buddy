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

    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Long id) {
        return subjectService.findSubjectById(id).orElseThrow();
    }

    @PostMapping("/")
    public Subject addSubject(@RequestBody SubjectRequest request) {
        Subject subject = new Subject();
        subject = subjectService.buildSubject(request);
        return subjectService.saveSubject(subject);
    }

    @PutMapping("/{id}")
    public Subject updateSubject(
            @RequestBody SubjectRequest request,
            @PathVariable Long id) {
        Subject subject = new Subject();
        // check if the user exists before doing an update
        Subject existingSubject = subjectService.findSubjectById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        existingSubject.setUser(userService.findUserById(request.getUserId()).orElseThrow());
        existingSubject.setName(request.getName());
        return subjectService.saveSubject(subject);
    }

    @DeleteMapping("/{id}")
    public void deleteSubjectById(@PathVariable Long id){
        subjectService.deleteSubjectById(id);
    }
}

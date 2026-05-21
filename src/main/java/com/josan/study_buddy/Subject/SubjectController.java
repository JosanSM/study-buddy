package com.josan.study_buddy.Subject;

import com.josan.study_buddy.Subject.SubjectDto.SubjectRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService)  {
        this.subjectService = subjectService;
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

    @PutMapping("/")
    public ResponseEntity<Subject> updateSubject(
            @RequestBody SubjectRequest request) {

        try {
            return ResponseEntity.ok(subjectService.updateSubject(request));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteSubjectById(@PathVariable Long id){
        subjectService.deleteSubjectById(id);
    }
}

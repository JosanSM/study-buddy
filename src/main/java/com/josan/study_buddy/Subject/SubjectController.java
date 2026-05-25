package com.josan.study_buddy.Subject;

import com.josan.study_buddy.Subject.SubjectDto.GenericSubjectResponse;
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
    public ResponseEntity<List<GenericSubjectResponse>> getAllSubjects() {

        return ResponseEntity.ok(subjectService.findAllSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericSubjectResponse> getSubjectById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(subjectService.findSubjectById(id));
        } catch (RuntimeException e) {
            return  ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public Subject addSubject(@RequestBody SubjectRequest request) {
        Subject subject = new Subject();
        subject = subjectService.buildSubject(request);
        return subjectService.saveSubject(subject);
    }

    @PutMapping("/")
    public ResponseEntity<GenericSubjectResponse> updateSubjectName(
            @RequestBody SubjectRequest request) {

        try {
            return ResponseEntity.ok(subjectService.updateSubjectName(request));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteSubjectById(@PathVariable Long id){
        subjectService.deleteSubjectById(id);
    }
}

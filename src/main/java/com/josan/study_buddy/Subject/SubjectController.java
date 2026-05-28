package com.josan.study_buddy.Subject;

import com.josan.study_buddy.Subject.SubjectDto.AddSubjectRequest;
import com.josan.study_buddy.Subject.SubjectDto.GenericSubjectResponse;
import com.josan.study_buddy.Subject.SubjectDto.SubjectRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GenericSubjectResponse> addSubject(@Valid @RequestBody AddSubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.addSubject(request));
    }

    @PutMapping("/")
    public ResponseEntity<GenericSubjectResponse> updateSubjectName(
            @Valid @RequestBody SubjectRequest request) {

        try {
            return ResponseEntity.ok(subjectService.updateSubjectName(request));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubjectById(@PathVariable Long id){
        subjectService.deleteSubjectById(id);
        return ResponseEntity.noContent().build();
    }
}

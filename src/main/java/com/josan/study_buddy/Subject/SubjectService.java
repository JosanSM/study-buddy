package com.josan.study_buddy.Subject;
import com.josan.study_buddy.Subject.SubjectDto.GenericSubjectResponse;
import com.josan.study_buddy.Subject.SubjectDto.SubjectRequest;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    SubjectRepository subjectRepository;
    UserService userService;
    
    public SubjectService(SubjectRepository subjectRepository, UserService userService) {
        this.subjectRepository = subjectRepository;
        this.userService = userService;
    }

    public List<GenericSubjectResponse> findAllSubjects() {

        return subjectRepository.findAll()
                .stream()
                .map(subject -> GenericSubjectResponse.builder()
                            .id(subject.getId())
                            .name(subject.getName())
                            .build())
                .toList();
    }

    public GenericSubjectResponse findSubjectById(Long id) {

        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No subject found"));

        return GenericSubjectResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .build();

    }

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(id);
    }

    public Subject buildSubject(SubjectRequest request) {
        Subject subject = new Subject();
        User user = userService.findUserById(request.getUserId()).orElseThrow();

    public GenericSubjectResponse updateSubjectName(SubjectRequest request) {
        // check if the user exists before doing an update
        Subject existingSubject = subjectRepository.findById(request.getSubjectId()).orElse(null);

        if(existingSubject == null) {
            throw new RuntimeException("Not found");
        }

        existingSubject.setName(request.getName());

        Subject updatedSubject = subjectRepository.save(existingSubject);

        try {
            return GenericSubjectResponse.builder()
                    .id(updatedSubject.getId())
                    .name(updatedSubject.getName())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to save the subject %s", existingSubject.toString()));
        }

    }
}

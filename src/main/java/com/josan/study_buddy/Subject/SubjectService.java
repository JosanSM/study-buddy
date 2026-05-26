package com.josan.study_buddy.Subject;
import com.josan.study_buddy.Subject.SubjectDto.AddSubjectRequest;
import com.josan.study_buddy.Subject.SubjectDto.GenericSubjectResponse;
import com.josan.study_buddy.Subject.SubjectDto.SubjectRequest;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // returns an Optional wit the actual entity for methods that rely on methods like .orElseThrow or .isEmpty which the DTOs don't have.
    public Subject findSubjectEntityById(Long id) {
        return subjectRepository.findById(id).orElseThrow(()-> new RuntimeException("Subject not found"));
    }

    @Transactional
    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Transactional
    public GenericSubjectResponse addSubject(AddSubjectRequest request) {
        User user = userService.findUserById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setUser(user);

        this.saveSubject(subject);

        return GenericSubjectResponse.from(subject);
    }

    @Transactional
    public void deleteSubjectById(Long id) {
        if(!subjectRepository.existsById(id)) {
            throw new RuntimeException("Subject not found");
        }
        subjectRepository.deleteById(id);
    }

    @Transactional
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

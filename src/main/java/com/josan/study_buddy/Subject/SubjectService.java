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
    private final SubjectRepository subjectRepository;
    private final UserService userService;

    public SubjectService(SubjectRepository subjectRepository, UserService userService) {
        this.subjectRepository = subjectRepository;
        this.userService = userService;
    }

    public List<GenericSubjectResponse> findAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(GenericSubjectResponse::from)
                .toList();
    }

    public GenericSubjectResponse findSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
        return GenericSubjectResponse.from(subject);
    }

    public Subject findSubjectEntityById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
    }

    @Transactional
    public GenericSubjectResponse addSubject(AddSubjectRequest request) {
        User user = userService.findUserEntityById(request.getUserId());
        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setUser(user);
        return GenericSubjectResponse.from(subjectRepository.save(subject));
    }

    @Transactional
    public void deleteSubjectById(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new RuntimeException("Subject not found with id: " + id);
        }
        subjectRepository.deleteById(id);
    }

    @Transactional
    public GenericSubjectResponse updateSubjectName(SubjectRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + request.getSubjectId()));
        subject.setName(request.getName());
        return GenericSubjectResponse.from(subjectRepository.save(subject));
    }
}

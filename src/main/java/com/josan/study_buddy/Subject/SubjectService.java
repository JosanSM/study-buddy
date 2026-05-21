package com.josan.study_buddy.Subject;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
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

    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> findSubjectById(Long id) {
        return subjectRepository.findById(id);
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

        subject.setName(request.getName());
        subject.setUser(user);
        return subject;
    }
}

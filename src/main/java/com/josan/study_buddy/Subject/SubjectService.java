package com.josan.study_buddy.Subject;

import com.josan.study_buddy.Subject.SubjectDto.AddSubjectRequest;
import com.josan.study_buddy.Subject.SubjectDto.GenericSubjectResponse;
import com.josan.study_buddy.Subject.SubjectDto.SubjectRequest;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import com.josan.study_buddy.exception.DuplicateSubjectNameException;
import com.josan.study_buddy.exception.SubjectNotFoundException;
import com.josan.study_buddy.exception.SubjectNotEmptyException;
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
        return subjectRepository.findById(id)
                .map(GenericSubjectResponse::from)
                .orElseThrow(() -> new SubjectNotFoundException(id));
    }

    public Subject findSubjectEntityById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
    }

    @Transactional
    public GenericSubjectResponse addSubject(AddSubjectRequest request) {
        User user = userService.findUserEntityById(request.getUserId());
        if (subjectRepository.existsByNameAndUserId(request.getName(), request.getUserId())) {
            throw new DuplicateSubjectNameException();
        }
        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setUser(user);
        return GenericSubjectResponse.from(subjectRepository.save(subject));
    }

    @Transactional
    public void deleteSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
        if (subject.getTopic() != null && !subject.getTopic().isEmpty()) {
            throw new SubjectNotEmptyException();
        }
        subjectRepository.deleteById(id);
    }

    @Transactional
    public GenericSubjectResponse updateSubjectName(SubjectRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(request.getSubjectId()));
        subject.setName(request.getName());
        return GenericSubjectResponse.from(subjectRepository.save(subject));
    }
}

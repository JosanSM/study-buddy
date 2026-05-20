package com.josan.study_buddy.Subject;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



/*
TODO: Consider creating a SubjectResponse object that is a DTO object. Ideally we don't return the Subject 
object to the user because that is a JPA entity that maps directly to a database. That can be different than the Subject
API response we want to return.
*/
@Service
// TOOD: Add @Transactional annotation here
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

    // TOOD: Consider adding @Transactional here for anything that saves to db
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
        /*
        TODO: This should not save the subject object into the database.
         It should only build the subject object. It is not implied that buildSubject saves the object to the database
         so you end up calling saveSubject twice (example being POST /subject endpoint)
        */
        return this.saveSubject(subject); 
    }
}

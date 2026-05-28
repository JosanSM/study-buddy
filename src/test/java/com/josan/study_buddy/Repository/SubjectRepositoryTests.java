package com.josan.study_buddy.Repository;

import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Subject.SubjectRepository;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SubjectRepositoryTests {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedUser() {
        return userRepository.save(User.builder()
                .name("test user")
                .email("testuser@gmail.com")
                .user_tier("pro")
                .build());
    }

    @Test
    public void SubjectRepository_saveSubject_ReturnsSavedSubject() {
        // Arrange
        User user = savedUser();
        Subject subject = Subject.builder()
                .name("Mathematics")
                .user(user)
                .build();

        // Act
        Subject savedSubject = subjectRepository.save(subject);

        // Assert
        Assertions.assertNotNull(savedSubject);
        Assertions.assertNotNull(savedSubject.getId());
        Assertions.assertEquals("Mathematics", savedSubject.getName());
        Assertions.assertEquals(user.getId(), savedSubject.getUser().getId());
    }

    @Test
    public void SubjectRepository_findById_ReturnsFoundSubject() {
        // Arrange
        User user = savedUser();
        Subject subject = Subject.builder()
                .name("History")
                .user(user)
                .build();
        subjectRepository.save(subject);

        // Act
        Subject foundSubject = subjectRepository.findById(subject.getId()).orElse(null);

        // Assert
        Assertions.assertNotNull(foundSubject);
        Assertions.assertEquals("History", foundSubject.getName());
        Assertions.assertEquals(user.getId(), foundSubject.getUser().getId());
    }

    @Test
    public void SubjectRepository_findById_ReturnsEmptyOptionalIfSubjectDoesNotExist() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        Subject foundSubject = subjectRepository.findById(nonExistentId).orElse(null);

        // Assert
        Assertions.assertNull(foundSubject);
    }

    @Test
    public void SubjectRepository_findAll_ReturnsAllSubjects() {
        // Arrange
        User user = savedUser();
        Subject subject1 = Subject.builder().name("Physics").user(user).build();
        Subject subject2 = Subject.builder().name("Chemistry").user(user).build();
        subjectRepository.save(subject1);
        subjectRepository.save(subject2);

        // Act
        List<Subject> allSubjects = subjectRepository.findAll();

        // Assert
        Assertions.assertNotNull(allSubjects);
        Assertions.assertEquals(2, allSubjects.size());
    }

    @Test
    public void SubjectRepository_deleteById_SubjectIsDeleted() {
        // Arrange
        User user = savedUser();
        Subject subject = Subject.builder()
                .name("Biology")
                .user(user)
                .build();
        subjectRepository.save(subject);

        // Act
        subjectRepository.deleteById(subject.getId());

        // Assert
        Assertions.assertFalse(subjectRepository.existsById(subject.getId()));
    }

    @Test
    public void SubjectRepository_existsById_ReturnsTrueIfSubjectExists() {
        // Arrange
        User user = savedUser();
        Subject subject = Subject.builder()
                .name("Geography")
                .user(user)
                .build();
        subjectRepository.save(subject);

        // Act
        boolean exists = subjectRepository.existsById(subject.getId());

        // Assert
        Assertions.assertTrue(exists);
    }

    @Test
    public void SubjectRepository_existsById_ReturnsFalseIfSubjectDoesNotExist() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        boolean exists = subjectRepository.existsById(nonExistentId);

        // Assert
        Assertions.assertFalse(exists);
    }

    @Test
    public void SubjectRepository_saveSubject_UpdatesExistingSubject() {
        // Arrange
        User user = savedUser();
        Subject subject = Subject.builder()
                .name("Old Name")
                .user(user)
                .build();
        subjectRepository.save(subject);
        subject.setName("New Name");

        // Act
        Subject updatedSubject = subjectRepository.save(subject);

        // Assert
        Assertions.assertEquals("New Name", updatedSubject.getName());
        Assertions.assertEquals(subject.getId(), updatedSubject.getId());
    }
}
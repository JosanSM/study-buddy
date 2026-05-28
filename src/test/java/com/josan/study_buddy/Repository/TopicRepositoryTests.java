package com.josan.study_buddy.Repository;

import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Subject.SubjectRepository;
import com.josan.study_buddy.Topic.Topic;
import com.josan.study_buddy.Topic.TopicRepository;
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
public class TopicRepositoryTests {

    @Autowired
    private TopicRepository topicRepository;

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

    private Subject savedSubject(User user) {
        return subjectRepository.save(Subject.builder()
                .name("Mathematics")
                .user(user)
                .build());
    }

    private Topic buildTopic(String title, String notes, String status, User user, Subject subject) {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setNotes(notes);
        topic.setTopicStatus(status);
        topic.setUser(user);
        topic.setSubject(subject);
        return topic;
    }

    @Test
    public void TopicRepository_saveTopic_ReturnsSavedTopic() {
        // Arrange
        User user = savedUser();
        Subject subject = savedSubject(user);
        Topic topic = buildTopic("Introduction to Algebra", "Chapter 1 notes", "IN_PROGRESS", user, subject);

        // Act
        Topic savedTopic = topicRepository.save(topic);

        // Assert
        Assertions.assertNotNull(savedTopic);
        Assertions.assertNotNull(savedTopic.getId());
        Assertions.assertEquals("Introduction to Algebra", savedTopic.getTitle());
        Assertions.assertEquals("Chapter 1 notes", savedTopic.getNotes());
        Assertions.assertEquals("IN_PROGRESS", savedTopic.getTopicStatus());
        Assertions.assertEquals(user.getId(), savedTopic.getUser().getId());
        Assertions.assertEquals(subject.getId(), savedTopic.getSubject().getId());
    }

    @Test
    public void TopicRepository_findById_ReturnsFoundTopic() {
        // Arrange
        User user = savedUser();
        Subject subject = savedSubject(user);
        Topic topic = buildTopic("Calculus Basics", "Derivatives notes", "NOT_STARTED", user, subject);
        topicRepository.save(topic);

        // Act
        Topic foundTopic = topicRepository.findById(topic.getId()).orElse(null);

        // Assert
        Assertions.assertNotNull(foundTopic);
        Assertions.assertEquals("Calculus Basics", foundTopic.getTitle());
        Assertions.assertEquals("Derivatives notes", foundTopic.getNotes());
    }

    @Test
    public void TopicRepository_findById_ReturnsEmptyOptionalIfTopicDoesNotExist() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        Topic foundTopic = topicRepository.findById(nonExistentId).orElse(null);

        // Assert
        Assertions.assertNull(foundTopic);
    }

    @Test
    public void TopicRepository_findAll_ReturnsAllTopics() {
        // Arrange
        User user = savedUser();
        Subject subject = savedSubject(user);
        Topic topic1 = buildTopic("Topic One", "Notes one", "NOT_STARTED", user, subject);
        Topic topic2 = buildTopic("Topic Two", "Notes two", "COMPLETED", user, subject);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        // Act
        List<Topic> allTopics = topicRepository.findAll();

        // Assert
        Assertions.assertNotNull(allTopics);
        Assertions.assertEquals(2, allTopics.size());
    }

    @Test
    public void TopicRepository_deleteById_TopicIsDeleted() {
        // Arrange
        User user = savedUser();
        Subject subject = savedSubject(user);
        Topic topic = buildTopic("To Be Deleted", "Some notes", "NOT_STARTED", user, subject);
        topicRepository.save(topic);

        // Act
        topicRepository.deleteById(topic.getId());

        // Assert
        Assertions.assertFalse(topicRepository.existsById(topic.getId()));
    }

    @Test
    public void TopicRepository_existsById_ReturnsTrueIfTopicExists() {
        // Arrange
        User user = savedUser();
        Subject subject = savedSubject(user);
        Topic topic = buildTopic("Existing Topic", "Some notes", "IN_PROGRESS", user, subject);
        topicRepository.save(topic);

        // Act
        boolean exists = topicRepository.existsById(topic.getId());

        // Assert
        Assertions.assertTrue(exists);
    }

    @Test
    public void TopicRepository_existsById_ReturnsFalseIfTopicDoesNotExist() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        boolean exists = topicRepository.existsById(nonExistentId);

        // Assert
        Assertions.assertFalse(exists);
    }

    @Test
    public void TopicRepository_saveTopic_UpdatesExistingTopic() {
        // Arrange
        User user = savedUser();
        Subject subject = savedSubject(user);
        Topic topic = buildTopic("Old Title", "Old notes", "NOT_STARTED", user, subject);
        topicRepository.save(topic);
        topic.setTitle("New Title");
        topic.setTopicStatus("COMPLETED");

        // Act
        Topic updatedTopic = topicRepository.save(topic);

        // Assert
        Assertions.assertEquals("New Title", updatedTopic.getTitle());
        Assertions.assertEquals("COMPLETED", updatedTopic.getTopicStatus());
        Assertions.assertEquals(topic.getId(), updatedTopic.getId());
    }
}

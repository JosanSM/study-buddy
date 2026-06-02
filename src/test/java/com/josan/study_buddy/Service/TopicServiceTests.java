package com.josan.study_buddy.Service;

import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Subject.SubjectService;
import com.josan.study_buddy.Topic.Topic;
import com.josan.study_buddy.Topic.TopicRepository;
import com.josan.study_buddy.Topic.TopicService;
import com.josan.study_buddy.Topic.TopicStatus;
import com.josan.study_buddy.Topic.TopicDto.GenericTopicResponse;
import com.josan.study_buddy.Topic.TopicDto.TopicRequest;
import com.josan.study_buddy.Topic.TopicDto.UpdateTopicRequest;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import com.josan.study_buddy.exception.DuplicateTopicTitleException;
import com.josan.study_buddy.exception.SubjectNotFoundException;
import com.josan.study_buddy.exception.SubjectUserMismatchException;
import com.josan.study_buddy.exception.TopicNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTests {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserService userService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private TopicService topicService;

    private User buildUser(long id) {
        return User.builder().id(id).name("User").email("user@example.com").user_tier("pro").build();
    }

    private Subject buildSubject(Long id, User owner) {
        return Subject.builder().id(id).name("Math").user(owner).build();
    }

    private Topic buildTopic(Long id, User user, Subject subject) {
        return Topic.builder()
                .id(id)
                .title("Derivatives")
                .notes("Some notes")
                .topicStatus(TopicStatus.NOT_STARTED)
                .user(user)
                .subject(subject)
                .build();
    }

    // findTopicById

    @Test
    public void findTopicById_TopicExists_ReturnsGenericTopicResponse() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        Topic topic = buildTopic(1L, user, subject);
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        GenericTopicResponse response = topicService.findTopicById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    public void findTopicById_TopicNotFound_ThrowsTopicNotFoundException() {
        when(topicRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TopicNotFoundException.class, () -> topicService.findTopicById(99L));
    }

    // addTopic

    @Test
    public void addTopic_ValidRequest_ReturnsGenericTopicResponse() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        Topic saved = buildTopic(1L, user, subject);
        TopicRequest request = TopicRequest.builder()
                .title("Derivatives")
                .notes("Some notes")
                .topicStatus(TopicStatus.NOT_STARTED)
                .userId(1L)
                .subjectId(1L)
                .build();

        when(userService.findUserEntityById(1L)).thenReturn(user);
        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);
        when(topicRepository.existsByTitleAndSubjectId("Derivatives", 1L)).thenReturn(false);
        when(topicRepository.save(any(Topic.class))).thenReturn(saved);

        GenericTopicResponse response = topicService.addTopic(request);

        assertNotNull(response);
        verify(topicRepository).save(any(Topic.class));
    }

    @Test
    public void addTopic_SubjectUserMismatch_ThrowsSubjectUserMismatchException() {
        User owner = buildUser(1L);
        User requester = buildUser(2L);
        Subject subject = buildSubject(1L, owner);
        TopicRequest request = TopicRequest.builder()
                .title("Derivatives")
                .notes("notes")
                .topicStatus(TopicStatus.NOT_STARTED)
                .userId(2L)
                .subjectId(1L)
                .build();

        when(userService.findUserEntityById(2L)).thenReturn(requester);
        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);

        assertThrows(SubjectUserMismatchException.class, () -> topicService.addTopic(request));
        verify(topicRepository, never()).save(any());
    }

    @Test
    public void addTopic_DuplicateTitle_ThrowsDuplicateTopicTitleException() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        TopicRequest request = TopicRequest.builder()
                .title("Derivatives")
                .notes("notes")
                .topicStatus(TopicStatus.NOT_STARTED)
                .userId(1L)
                .subjectId(1L)
                .build();

        when(userService.findUserEntityById(1L)).thenReturn(user);
        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);
        when(topicRepository.existsByTitleAndSubjectId("Derivatives", 1L)).thenReturn(true);

        assertThrows(DuplicateTopicTitleException.class, () -> topicService.addTopic(request));
        verify(topicRepository, never()).save(any());
    }

    @Test
    public void addTopic_SubjectNotFound_ThrowsSubjectNotFoundException() {
        User user = buildUser(1L);
        TopicRequest request = TopicRequest.builder()
                .title("Derivatives")
                .notes("notes")
                .topicStatus(TopicStatus.NOT_STARTED)
                .userId(1L)
                .subjectId(99L)
                .build();

        when(userService.findUserEntityById(1L)).thenReturn(user);
        when(subjectService.findSubjectEntityById(99L)).thenThrow(new SubjectNotFoundException(99L));

        assertThrows(SubjectNotFoundException.class, () -> topicService.addTopic(request));
    }

    // updateTopic

    @Test
    public void updateTopic_ValidRequest_ReturnsUpdatedResponse() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        Topic existing = buildTopic(1L, user, subject);
        UpdateTopicRequest request = UpdateTopicRequest.builder()
                .id(1L)
                .title("Integrals")
                .notes("updated notes")
                .topicStatus(TopicStatus.IN_PROGRESS)
                .userId(1L)
                .subjectId(1L)
                .build();

        when(topicRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userService.findUserEntityById(1L)).thenReturn(user);
        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);
        when(topicRepository.existsByTitleAndSubjectIdAndIdNot("Integrals", 1L, 1L)).thenReturn(false);
        when(topicRepository.save(existing)).thenReturn(existing);

        GenericTopicResponse response = topicService.updateTopic(request);

        assertNotNull(response);
        verify(topicRepository).save(existing);
    }

    @Test
    public void updateTopic_TopicNotFound_ThrowsTopicNotFoundException() {
        UpdateTopicRequest request = UpdateTopicRequest.builder()
                .id(99L).title("X").notes("n").topicStatus(TopicStatus.NOT_STARTED).userId(1L).subjectId(1L)
                .build();
        when(topicRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TopicNotFoundException.class, () -> topicService.updateTopic(request));
    }

    @Test
    public void updateTopic_SubjectUserMismatch_ThrowsSubjectUserMismatchException() {
        User owner = buildUser(1L);
        User requester = buildUser(2L);
        Subject subject = buildSubject(1L, owner);
        Topic existing = buildTopic(1L, owner, subject);
        UpdateTopicRequest request = UpdateTopicRequest.builder()
                .id(1L).title("X").notes("n").topicStatus(TopicStatus.NOT_STARTED).userId(2L).subjectId(1L)
                .build();

        when(topicRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userService.findUserEntityById(2L)).thenReturn(requester);
        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);

        assertThrows(SubjectUserMismatchException.class, () -> topicService.updateTopic(request));
    }

    @Test
    public void updateTopic_DuplicateTitle_ThrowsDuplicateTopicTitleException() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        Topic existing = buildTopic(1L, user, subject);
        UpdateTopicRequest request = UpdateTopicRequest.builder()
                .id(1L).title("Integrals").notes("n").topicStatus(TopicStatus.NOT_STARTED).userId(1L).subjectId(1L)
                .build();

        when(topicRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userService.findUserEntityById(1L)).thenReturn(user);
        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);
        when(topicRepository.existsByTitleAndSubjectIdAndIdNot("Integrals", 1L, 1L)).thenReturn(true);

        assertThrows(DuplicateTopicTitleException.class, () -> topicService.updateTopic(request));
    }

    // deleteTopicById

    @Test
    public void deleteTopicById_TopicExists_DeletesTopic() {
        when(topicRepository.existsById(1L)).thenReturn(true);

        topicService.deleteTopicById(1L);

        verify(topicRepository).deleteById(1L);
    }

    @Test
    public void deleteTopicById_TopicNotFound_ThrowsTopicNotFoundException() {
        when(topicRepository.existsById(99L)).thenReturn(false);

        assertThrows(TopicNotFoundException.class, () -> topicService.deleteTopicById(99L));
        verify(topicRepository, never()).deleteById(any());
    }
}

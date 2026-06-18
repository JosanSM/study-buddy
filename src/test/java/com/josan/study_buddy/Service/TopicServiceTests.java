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
import com.josan.study_buddy.config.SpacedRepetitionProperties;
import com.josan.study_buddy.exception.DuplicateTopicTitleException;
import com.josan.study_buddy.exception.SubjectNotFoundException;
import com.josan.study_buddy.exception.SubjectUserMismatchException;
import com.josan.study_buddy.exception.TopicNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTests {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private SubjectService subjectService;

    @Mock
    private SpacedRepetitionProperties spacedRepetitionProperties;

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
                .subjectId(1L)
                .build();

        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);
        when(topicRepository.existsByTitleAndSubjectId("Derivatives", 1L)).thenReturn(false);
        when(topicRepository.save(any(Topic.class))).thenReturn(saved);

        GenericTopicResponse response = topicService.addTopic(request, user);

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
                .subjectId(1L)
                .build();

        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);

        assertThrows(SubjectUserMismatchException.class, () -> topicService.addTopic(request, requester));
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
                .subjectId(1L)
                .build();

        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);
        when(topicRepository.existsByTitleAndSubjectId("Derivatives", 1L)).thenReturn(true);

        assertThrows(DuplicateTopicTitleException.class, () -> topicService.addTopic(request, user));
        verify(topicRepository, never()).save(any());
    }

    @Test
    public void addTopic_SubjectNotFound_ThrowsSubjectNotFoundException() {
        User user = buildUser(1L);
        TopicRequest request = TopicRequest.builder()
                .title("Derivatives")
                .notes("notes")
                .topicStatus(TopicStatus.NOT_STARTED)
                .subjectId(99L)
                .build();

        when(subjectService.findSubjectEntityById(99L)).thenThrow(new SubjectNotFoundException(99L));

        assertThrows(SubjectNotFoundException.class, () -> topicService.addTopic(request, user));
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
                .subjectId(1L)
                .build();

        when(topicRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);
        when(topicRepository.existsByTitleAndSubjectIdAndIdNot("Integrals", 1L, 1L)).thenReturn(false);
        when(topicRepository.save(existing)).thenReturn(existing);

        GenericTopicResponse response = topicService.updateTopic(request, user);

        assertNotNull(response);
        verify(topicRepository).save(existing);
    }

    @Test
    public void updateTopic_TopicNotFound_ThrowsTopicNotFoundException() {
        User user = buildUser(1L);
        UpdateTopicRequest request = UpdateTopicRequest.builder()
                .id(99L).title("X").notes("n").topicStatus(TopicStatus.NOT_STARTED).subjectId(1L)
                .build();
        when(topicRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TopicNotFoundException.class, () -> topicService.updateTopic(request, user));
    }

    @Test
    public void updateTopic_TopicOwnerMismatch_ThrowsSubjectUserMismatchException() {
        User owner = buildUser(1L);
        User requester = buildUser(2L);
        Subject subject = buildSubject(1L, owner);
        Topic existing = buildTopic(1L, owner, subject);
        UpdateTopicRequest request = UpdateTopicRequest.builder()
                .id(1L).title("X").notes("n").topicStatus(TopicStatus.NOT_STARTED).subjectId(1L)
                .build();

        when(topicRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(SubjectUserMismatchException.class, () -> topicService.updateTopic(request, requester));
        verify(topicRepository, never()).save(any());
    }

    @Test
    public void updateTopic_SubjectUserMismatch_ThrowsSubjectUserMismatchException() {
        User user = buildUser(1L);
        User otherUser = buildUser(2L);
        Subject subjectOwnedByOther = buildSubject(1L, otherUser);
        Topic existing = buildTopic(1L, user, buildSubject(2L, user));
        UpdateTopicRequest request = UpdateTopicRequest.builder()
                .id(1L).title("X").notes("n").topicStatus(TopicStatus.NOT_STARTED).subjectId(1L)
                .build();

        when(topicRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(subjectService.findSubjectEntityById(1L)).thenReturn(subjectOwnedByOther);

        assertThrows(SubjectUserMismatchException.class, () -> topicService.updateTopic(request, user));
        verify(topicRepository, never()).save(any());
    }

    @Test
    public void updateTopic_DuplicateTitle_ThrowsDuplicateTopicTitleException() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        Topic existing = buildTopic(1L, user, subject);
        UpdateTopicRequest request = UpdateTopicRequest.builder()
                .id(1L).title("Integrals").notes("n").topicStatus(TopicStatus.NOT_STARTED).subjectId(1L)
                .build();

        when(topicRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(subjectService.findSubjectEntityById(1L)).thenReturn(subject);
        when(topicRepository.existsByTitleAndSubjectIdAndIdNot("Integrals", 1L, 1L)).thenReturn(true);

        assertThrows(DuplicateTopicTitleException.class, () -> topicService.updateTopic(request, user));
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

    // increaseReviewCount

    @Test
    public void increaseReviewCount_FirstReview_UsesFirstIntervalAndIncrementsCount() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        Topic topic = buildTopic(1L, user, subject);
        List<Integer> intervals = List.of(1, 3, 7, 14, 30, 60);

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(spacedRepetitionProperties.getIntervals()).thenReturn(intervals);
        when(topicRepository.save(topic)).thenReturn(topic);

        GenericTopicResponse response = topicService.increaseReviewCount(1L, user);

        assertEquals(1, response.getReviewCount());
        assertEquals(LocalDate.now(), response.getLastReviewedAt());
        assertEquals(LocalDate.now().plusDays(1), response.getNextReviewAt());
    }

    @Test
    public void increaseReviewCount_SecondReview_UsesSecondInterval() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        Topic topic = Topic.builder()
                .id(1L).title("Derivatives").notes("notes")
                .topicStatus(TopicStatus.NOT_STARTED)
                .user(user).subject(subject)
                .reviewCount(1)
                .build();
        List<Integer> intervals = List.of(1, 3, 7, 14, 30, 60);

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(spacedRepetitionProperties.getIntervals()).thenReturn(intervals);
        when(topicRepository.save(topic)).thenReturn(topic);

        GenericTopicResponse response = topicService.increaseReviewCount(1L, user);

        assertEquals(2, response.getReviewCount());
        assertEquals(LocalDate.now().plusDays(3), response.getNextReviewAt());
    }

    @Test
    public void increaseReviewCount_ReviewCountExceedsIntervals_UsesLastInterval() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        Topic topic = Topic.builder()
                .id(1L).title("Derivatives").notes("notes")
                .topicStatus(TopicStatus.NOT_STARTED)
                .user(user).subject(subject)
                .reviewCount(100)
                .build();
        List<Integer> intervals = List.of(1, 3, 7, 14, 30, 60);

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(spacedRepetitionProperties.getIntervals()).thenReturn(intervals);
        when(topicRepository.save(topic)).thenReturn(topic);

        GenericTopicResponse response = topicService.increaseReviewCount(1L, user);

        assertEquals(101, response.getReviewCount());
        assertEquals(LocalDate.now().plusDays(60), response.getNextReviewAt());
    }

    @Test
    public void increaseReviewCount_WrongUser_ThrowsSubjectUserMismatchException() {
        User owner = buildUser(1L);
        User requester = buildUser(2L);
        Subject subject = buildSubject(1L, owner);
        Topic topic = buildTopic(1L, owner, subject);

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        assertThrows(SubjectUserMismatchException.class,
                () -> topicService.increaseReviewCount(1L, requester));
        verify(topicRepository, never()).save(any());
    }

    @Test
    public void increaseReviewCount_TopicNotFound_ThrowsTopicNotFoundException() {
        User user = buildUser(1L);
        when(topicRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TopicNotFoundException.class,
                () -> topicService.increaseReviewCount(99L, user));
    }

    // findDueTopics

    @Test
    public void findDueTopics_WithDueTopics_ReturnsDueTopics() {
        User user = buildUser(1L);
        Subject subject = buildSubject(1L, user);
        Topic due = Topic.builder()
                .id(1L).title("Derivatives").notes("notes")
                .topicStatus(TopicStatus.IN_PROGRESS)
                .user(user).subject(subject)
                .reviewCount(2)
                .lastReviewedAt(LocalDate.now().minusDays(3))
                .nextReviewAt(LocalDate.now().minusDays(1))
                .build();

        when(topicRepository.findByUserIdAndNextReviewAtLessThanEqual(1L, LocalDate.now()))
                .thenReturn(List.of(due));

        List<GenericTopicResponse> result = topicService.findDueTopics(user);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    public void findDueTopics_NoDueTopics_ReturnsEmptyList() {
        User user = buildUser(1L);
        when(topicRepository.findByUserIdAndNextReviewAtLessThanEqual(1L, LocalDate.now()))
                .thenReturn(List.of());

        List<GenericTopicResponse> result = topicService.findDueTopics(user);

        assertTrue(result.isEmpty());
    }
}
package com.josan.study_buddy.Topic;

import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Subject.SubjectService;
import com.josan.study_buddy.Topic.TopicDto.GenericTopicResponse;
import com.josan.study_buddy.Topic.TopicDto.TopicRequest;
import com.josan.study_buddy.Topic.TopicDto.UpdateTopicRequest;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.config.SpacedRepetitionProperties;
import com.josan.study_buddy.exception.DuplicateTopicTitleException;
import com.josan.study_buddy.exception.SubjectUserMismatchException;
import com.josan.study_buddy.exception.TopicNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final SubjectService subjectService;
    private final SpacedRepetitionProperties spacedRepetitionProperties;

    public TopicService(TopicRepository topicRepository, SubjectService subjectService,
                        SpacedRepetitionProperties spacedRepetitionProperties) {
        this.topicRepository = topicRepository;
        this.subjectService = subjectService;
        this.spacedRepetitionProperties = spacedRepetitionProperties;
    }

    @Transactional(readOnly = true)
    public List<GenericTopicResponse> findAllTopics() {
        return topicRepository.findAll()
                .stream()
                .map(GenericTopicResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GenericTopicResponse findTopicById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException(id));
        return GenericTopicResponse.from(topic);
    }

    @Transactional
    public GenericTopicResponse addTopic(TopicRequest request, User authenticatedUser) {
        Subject subject = subjectService.findSubjectEntityById(request.getSubjectId());

        if (subject.getUser().getId() != authenticatedUser.getId()) {
            throw new SubjectUserMismatchException();
        }
        if (topicRepository.existsByTitleAndSubjectId(request.getTitle(), request.getSubjectId())) {
            throw new DuplicateTopicTitleException();
        }

        Topic topic = new Topic();
        topic.setTitle(request.getTitle());
        topic.setNotes(request.getNotes());
        topic.setTopicStatus(request.getTopicStatus());
        topic.setUser(authenticatedUser);
        topic.setSubject(subject);

        return GenericTopicResponse.from(topicRepository.save(topic));
    }

    @Transactional
    public GenericTopicResponse updateTopic(UpdateTopicRequest request, User authenticatedUser) {
        Topic existing = findTopicEntityById(request.getId());
        Subject subject = subjectService.findSubjectEntityById(request.getSubjectId());

        if (existing.getUser().getId() != authenticatedUser.getId()) {
            throw new SubjectUserMismatchException();
        }
        if (subject.getUser().getId() != authenticatedUser.getId()) {
            throw new SubjectUserMismatchException();
        }
        if (topicRepository.existsByTitleAndSubjectIdAndIdNot(request.getTitle(), request.getSubjectId(), request.getId())) {
            throw new DuplicateTopicTitleException();
        }

        existing.setTopicStatus(request.getTopicStatus());
        existing.setNotes(request.getNotes());
        existing.setTitle(request.getTitle());
        existing.setUser(authenticatedUser);
        existing.setSubject(subject);

        return GenericTopicResponse.from(topicRepository.save(existing));
    }

    @Transactional
    public void deleteTopicById(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new TopicNotFoundException(id);
        }
        topicRepository.deleteById(id);
    }

    @Transactional
    public GenericTopicResponse increaseReviewCount(Long topicId, User authenticatedUser) {
        Topic topic = findTopicEntityById(topicId);

        if (topic.getUser().getId() != authenticatedUser.getId()) {
            throw new SubjectUserMismatchException();
        }

        List<Integer> intervals = spacedRepetitionProperties.getIntervals();
        int intervalIndex = Math.min(topic.getReviewCount(), intervals.size() - 1);
        int daysUntilNext = intervals.get(intervalIndex);

        LocalDate today = LocalDate.now();
        topic.setLastReviewedAt(today);
        topic.setNextReviewAt(today.plusDays(daysUntilNext));
        topic.setReviewCount(topic.getReviewCount() + 1);

        return GenericTopicResponse.from(topicRepository.save(topic));
    }

    @Transactional(readOnly = true)
    public List<GenericTopicResponse> findDueTopics(User authenticatedUser) {
        return topicRepository.findByUserIdAndNextReviewAtLessThanEqual(authenticatedUser.getId(), LocalDate.now())
                .stream()
                .map(GenericTopicResponse::from)
                .toList();
    }

    private Topic findTopicEntityById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException(id));
    }

}

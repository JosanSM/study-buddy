package com.josan.study_buddy.Topic;

import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Subject.SubjectService;
import com.josan.study_buddy.Topic.TopicDto.GenericTopicResponse;
import com.josan.study_buddy.Topic.TopicDto.TopicRequest;
import com.josan.study_buddy.Topic.TopicDto.UpdateTopicRequest;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import com.josan.study_buddy.exception.DuplicateTopicTitleException;
import com.josan.study_buddy.exception.SubjectUserMismatchException;
import com.josan.study_buddy.exception.TopicNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserService userService;
    private final SubjectService subjectService;

    public TopicService(TopicRepository topicRepository, UserService userService, SubjectService subjectService) {
        this.topicRepository = topicRepository;
        this.userService = userService;
        this.subjectService = subjectService;
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
    public GenericTopicResponse addTopic(TopicRequest request) {
        User user = userService.findUserEntityById(request.getUserId());
        Subject subject = subjectService.findSubjectEntityById(request.getSubjectId());

        if (subject.getUser().getId() != request.getUserId()) {
            throw new SubjectUserMismatchException();
        }
        if (topicRepository.existsByTitleAndSubjectId(request.getTitle(), request.getSubjectId())) {
            throw new DuplicateTopicTitleException();
        }

        Topic topic = new Topic();
        topic.setTitle(request.getTitle());
        topic.setNotes(request.getNotes());
        topic.setTopicStatus(request.getTopicStatus());
        topic.setUser(user);
        topic.setSubject(subject);

        return GenericTopicResponse.from(topicRepository.save(topic));
    }

    @Transactional
    public GenericTopicResponse updateTopic(UpdateTopicRequest request) {
        Topic existing = findTopicEntityById(request.getId());
        User user = userService.findUserEntityById(request.getUserId());
        Subject subject = subjectService.findSubjectEntityById(request.getSubjectId());

        if (subject.getUser().getId() != request.getUserId()) {
            throw new SubjectUserMismatchException();
        }
        if (topicRepository.existsByTitleAndSubjectIdAndIdNot(request.getTitle(), request.getSubjectId(), request.getId())) {
            throw new DuplicateTopicTitleException();
        }

        existing.setTopicStatus(request.getTopicStatus());
        existing.setNotes(request.getNotes());
        existing.setTitle(request.getTitle());
        existing.setUser(user);
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

    private Topic findTopicEntityById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException(id));
    }

}

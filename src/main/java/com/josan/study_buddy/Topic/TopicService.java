package com.josan.study_buddy.Topic;
import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Subject.SubjectService;
import com.josan.study_buddy.Topic.TopicDto.TopicRequest;
import com.josan.study_buddy.Topic.TopicDto.UpdateTopicRequest;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Topic> findAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> findTopicById(Long id) {
        return topicRepository.findById(id);
    }

    public Topic saveTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public void deleteTopicById(Long id) {
        topicRepository.deleteById(id);
    }

    public Topic buildTopic(TopicRequest request) {
        Topic topic = new Topic();

        Subject subject = subjectService.findSubjectById(request.getSubjectId()).orElseThrow();
        User user = userService.findUserById(request.getUserId()).orElseThrow();

        topic.setTopicStatus(request.getTopicStatus());
        topic.setTitle(request.getTitle());
        topic.setNotes(request.getNotes());
        topic.setSubject(subject);
        topic.setUser(user);

        return topic;
    }

    public Topic updateTopic(UpdateTopicRequest request) {
        Topic existing = this.findTopicById(request.getId())
                .orElseThrow(() -> new RuntimeException(String.format("No topic found with id %d",request.getId())));

        User user = userService.findUserById(request.getUserId()).orElseThrow();
        Subject subject = subjectService.findSubjectById(request.getSubjectId()).orElseThrow();

        existing.setTopicStatus(request.getTopicStatus());
        existing.setNotes(request.getNotes());
        existing.setTitle(request.getTitle());
        existing.setUser(user);
        existing.setSubject(subject);

        return this.saveTopic(existing);
    }

    public Boolean existsById(Long id) {
        return topicRepository.existsById(id);
    }
}

package com.josan.study_buddy.Topic;
import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Subject.SubjectService;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;
    private final UserService userService;
    private final SubjectService subjectService;

    public TopicController(TopicService topicService, UserService userService, SubjectService subjectService) {
        this.topicService = topicService;
        this.userService = userService;
        this.subjectService = subjectService;
    }

    @GetMapping("/")
    public List<Topic> getAllTopics() {
        return topicService.findAllTopics();
    }

    @PostMapping("/")
    public Topic addTopic(@RequestBody TopicRequest request){ //TODO: clarify if this should be abstracted out into the service layer
        Topic topic = new Topic();
        User user = userService.findUserById(request.getUserId()).orElseThrow();
        Subject subject = subjectService.findSubjectById(request.getSubjectId()).orElseThrow();

        topic.setTitle(request.getTitle());
        topic.setNotes(request.getNotes());
        topic.setTopicStatus(request.getTopicStatus());
        topic.setUser(user);
        topic.setSubject(subject);

        return topicService.saveTopic(topic);
    }
}

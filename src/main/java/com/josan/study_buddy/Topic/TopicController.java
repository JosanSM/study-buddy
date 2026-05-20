package com.josan.study_buddy.Topic;
import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Subject.SubjectService;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/{id}")
    public Topic getTopicById(@PathVariable Long id) {
        return topicService.findTopicById(id).orElseThrow();
    }

    @PostMapping("/")
    public Topic addTopic(@RequestBody TopicRequest request){
        Topic topic = topicService.buildTopic(request);
        return topicService.saveTopic(topic);
    }

    @PutMapping("/{id}")
    public Topic updateTopic(
            @RequestBody TopicRequest request,
            @PathVariable Long id) {
        
        /*
        TODO: We need to move a lot of this logic to the TopicService class. This method finds topics, finds user,
        updates topic, then saves it. All of this should live in topicService.updateTopic() method
        */
        Topic existing = topicService.findTopicById(id)
                .orElseThrow(() -> new RuntimeException(String.format("No topic found with id %d",id)));

        User user = userService.findUserById(request.getUserId()).orElseThrow();
        Subject subject = subjectService.findSubjectById(request.getSubjectId()).orElseThrow();

        // TODO: look into builder pattern
        existing.setTopicStatus(request.getTopicStatus());
        existing.setNotes(request.getNotes());
        existing.setTitle(request.getTitle());
        existing.setUser(user);
        existing.setSubject(subject);

        return topicService.saveTopic(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopicById(@PathVariable Long id) {
        //TODO: I would move this check into the service method. that way, any service that wants to deleteTopicById will have that check in place
        if(!topicService.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Topic not found"
            );
        }
        topicService.deleteTopicById(id);
        return ResponseEntity.noContent().build();
    }
}

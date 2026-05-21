package com.josan.study_buddy.Topic;
import com.josan.study_buddy.Subject.SubjectService;
import com.josan.study_buddy.Topic.TopicDto.TopicRequest;
import com.josan.study_buddy.Topic.TopicDto.UpdateTopicRequest;
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

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
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

    @PutMapping("/")
    public ResponseEntity<Topic> updateTopic(
            @RequestBody UpdateTopicRequest request) {
        try {
            return ResponseEntity.ok(topicService.updateTopic(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); //TODO: add validation fr not found scenarios, return 404 for those
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopicById(@PathVariable Long id) { //TODO: Revise logic - why is it returning no content in happy path?
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

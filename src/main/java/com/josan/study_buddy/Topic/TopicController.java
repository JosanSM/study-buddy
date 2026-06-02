package com.josan.study_buddy.Topic;

import com.josan.study_buddy.Topic.TopicDto.GenericTopicResponse;
import com.josan.study_buddy.Topic.TopicDto.TopicRequest;
import com.josan.study_buddy.Topic.TopicDto.UpdateTopicRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/")
    public ResponseEntity<List<GenericTopicResponse>> getAllTopics() {
        return ResponseEntity.ok(topicService.findAllTopics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericTopicResponse> getTopicById(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.findTopicById(id));
    }

    @PostMapping("/")
    public ResponseEntity<GenericTopicResponse> addTopic(@Valid @RequestBody TopicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.addTopic(request));
    }

    @PutMapping("/")
    public ResponseEntity<GenericTopicResponse> updateTopic(@Valid @RequestBody UpdateTopicRequest request) {
        return ResponseEntity.ok(topicService.updateTopic(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopicById(@PathVariable Long id) {
        topicService.deleteTopicById(id);
        return ResponseEntity.noContent().build();
    }
}

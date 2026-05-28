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
        try {
            return ResponseEntity.ok(topicService.findTopicById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<GenericTopicResponse> addTopic(@Valid @RequestBody TopicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.addTopic(request));
    }

    @PutMapping("/")
    public ResponseEntity<GenericTopicResponse> updateTopic(@Valid @RequestBody UpdateTopicRequest request) {
        try {
            return ResponseEntity.ok(topicService.updateTopic(request));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopicById(@PathVariable Long id) {
        try {
            topicService.deleteTopicById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

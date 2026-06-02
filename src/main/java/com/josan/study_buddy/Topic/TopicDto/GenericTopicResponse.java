package com.josan.study_buddy.Topic.TopicDto;

import com.josan.study_buddy.Topic.Topic;
import com.josan.study_buddy.Topic.TopicStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericTopicResponse {
    private Long id;
    private String title;
    private String notes;
    private TopicStatus topicStatus;
    private Long subjectId;
    private Long userId;

    public static GenericTopicResponse from(Topic topic) {
        return GenericTopicResponse.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .notes(topic.getNotes())
                .topicStatus(topic.getTopicStatus())
                .subjectId(topic.getSubject() != null ? topic.getSubject().getId() : null)
                .userId(topic.getUser() != null ? topic.getUser().getId() : null)
                .build();
    }
}
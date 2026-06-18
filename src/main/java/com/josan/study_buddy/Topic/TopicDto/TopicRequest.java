package com.josan.study_buddy.Topic.TopicDto;

import com.josan.study_buddy.Topic.TopicStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicRequest {
    @NotBlank
    @Size(max = 120)
    private String title;
    @NotBlank
    private String notes;
    @NotNull
    private TopicStatus topicStatus;
    @NotNull
    private Long subjectId;
}

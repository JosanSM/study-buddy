package com.josan.study_buddy.Topic.TopicDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTopicRequest {
    @NotNull
    private Long id;
    @NotBlank
    @Max(value = 50)
    private String title;
    @NotBlank
    private String notes;
    @NotBlank
    private String topicStatus;
    @NotNull
    private Long userId;
    @NotNull
    private Long subjectId;
}

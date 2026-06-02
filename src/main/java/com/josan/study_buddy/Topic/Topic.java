package com.josan.study_buddy.Topic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.User.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String title;
    private String notes;
    @Enumerated(EnumType.STRING)
    private TopicStatus topicStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
}

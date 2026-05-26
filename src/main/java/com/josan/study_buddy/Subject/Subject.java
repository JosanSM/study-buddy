package com.josan.study_buddy.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.josan.study_buddy.Topic.Topic;
import com.josan.study_buddy.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "subject")
    private Set<Topic> topic;

    public Subject(Long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }
}

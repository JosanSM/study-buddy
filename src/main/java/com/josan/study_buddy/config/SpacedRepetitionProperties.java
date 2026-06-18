package com.josan.study_buddy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spaced-repetition")
public class SpacedRepetitionProperties {
    private List<Integer> intervals = List.of(1, 3, 7, 14, 30, 60);
}

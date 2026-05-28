package com.josan.study_buddy.Subject.SubjectDto;

import com.josan.study_buddy.Subject.Subject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GenericSubjectResponse {
    @NotNull
    private Long id;
    @NotBlank
    private String name;

    public static GenericSubjectResponse from (Subject subject) {
        return new GenericSubjectResponse(
                subject.getId(),
                subject.getName()
        );
    }
}

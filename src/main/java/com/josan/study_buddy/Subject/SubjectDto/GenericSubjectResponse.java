package com.josan.study_buddy.Subject.SubjectDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tools.jackson.databind.ext.QNameSerializer;

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
}

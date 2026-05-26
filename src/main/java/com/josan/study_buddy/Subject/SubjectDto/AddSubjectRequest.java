package com.josan.study_buddy.Subject.SubjectDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSubjectRequest {
    @NotBlank
    private String name;
    @NotNull
    private Long userId;
}

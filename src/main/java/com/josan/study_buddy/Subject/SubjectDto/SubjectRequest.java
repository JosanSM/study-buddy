package com.josan.study_buddy.Subject.SubjectDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {
    @NotNull
    private Long userId;
    @NotBlank
    private String name;
}

package ru.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {

    private Long id;

    @NotNull(message = "candidateId: required")
    private Long candidateId;

    @NotNull(message = "vacancyId: required")
    private Long vacancyId;

    @NotNull(message = "status: required")
    private String status; // CREATED | IN_REVIEW | INTERVIEW_SCHEDULED | REJECTED | OFFERED
}

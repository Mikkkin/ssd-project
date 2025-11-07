package ru.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class VacancyDTO {
    private Long id;

    @NotBlank @Size(max = 150)
    private String title;

    @NotBlank @Size(max = 5000)
    private String description;

    @NotBlank @Size(max = 100)
    private String department;

    @NotNull @Min(0)
    private Integer salaryFrom;

    @NotNull @Min(0)
    private Integer salaryTo;

    @NotBlank
    private String status; // DRAFT | PUBLISHED | CLOSED
}

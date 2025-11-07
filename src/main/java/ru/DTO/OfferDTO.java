package ru.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {

    private Long id;

    @NotNull(message = "applicationId: required")
    private Long applicationId;

    @Min(value = 0, message = "compensation: must be >= 0")
    private Integer compensation;

    @NotNull(message = "status: required")
    private String status; // CREATED | SENT | ACCEPTED | REJECTED | EXPIRED
}

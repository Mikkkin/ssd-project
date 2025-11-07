package ru.DTO;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDTO {

    private Long id;

    @NotNull(message = "applicationId: required")
    private Long applicationId;   // заявка, по которой назначается интервью

    @NotNull(message = "interviewerId: required")
    private Long interviewerId;   // слоты не пересекаются для этого интервьюера

    @NotNull(message = "startTime: required")
    private Instant startTime;

    @NotNull(message = "endTime: required")
    private Instant endTime;

    @Size(max = 200, message = "location: max 200")
    private String location;

    @NotNull(message = "status: required")
    private String status;        // SCHEDULED | COMPLETED | CANCELED
}

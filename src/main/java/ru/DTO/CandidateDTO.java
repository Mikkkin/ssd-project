package ru.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {

    private Long id;

    @NotBlank(message = "firstName: required")
    @Size(max = 100, message = "firstName: max 100")
    private String firstName;

    @NotBlank(message = "lastName: required")
    @Size(max = 100, message = "lastName: max 100")
    private String lastName;

    @NotBlank(message = "email: required")
    @Email(message = "email: invalid")
    @Size(max = 254, message = "email: max 254")
    private String email;

    @Size(max = 20, message = "phone: max 20")
    private String phone;

    @Size(max = 200, message = "positionWanted: max 200")
    private String positionWanted;
}

package app.microservice.employee.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class EmployeeUpdateDTO {
    @NonNull
    private Integer id;

    @NonNull
    @Size(min = 3, max = 100)
    private String fullName;
}

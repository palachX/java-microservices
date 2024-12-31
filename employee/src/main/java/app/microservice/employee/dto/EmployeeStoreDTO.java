package app.microservice.employee.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class EmployeeStoreDTO {
    @NonNull
    @Size(min = 1, max = 100)
    private String fullName;

    @NonNull
    @Size(min = 11, max = 11)
    private String phone;
}

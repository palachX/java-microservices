package app.microservice.employee.request;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;


public record EmployeeUpdateRequest(
    @NotNull(message = "{error.crud.full_name_is_null}")
    @Size(min = 3, max = 100, message = "{error.crud.full_name_length}")
    String fullName
) {

}

package app.microservice.employee.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeStoreRequest(
        @NotNull(message = "{error.crud.full_name_is_null}")
        @Size(min = 3, max = 100, message = "{error.crud.full_name_length}")
        String fullName,

        @NotNull
        @Size(min = 11, max = 11)
        String phone
) {
}

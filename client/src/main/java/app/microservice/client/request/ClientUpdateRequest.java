package app.microservice.client.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientUpdateRequest(
        @NotNull(message = "{error.crud.full_name_is_null}")
        @Size(min = 1, max = 100, message = "{error.crud.full_name_length}")
        String fullName,

        @NotNull
        @Size(min = 11, max = 11)
        String phone,

        @Size(min = 1, max = 100)
        String address,

        @Size(min = 6, max = 100)
        String email
) {
}

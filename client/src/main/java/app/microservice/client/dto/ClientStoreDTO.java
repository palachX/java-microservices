package app.microservice.client.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class ClientStoreDTO {

    @NonNull
    @Size(min = 1, max = 100)
    private String fullName;

    @NonNull
    @Size(min = 11, max = 11)
    private String phone;

    private String address;

    private String email;
}

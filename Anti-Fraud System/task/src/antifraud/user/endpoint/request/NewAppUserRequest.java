package antifraud.user.endpoint.request;

import jakarta.validation.constraints.NotBlank;

public record NewAppUserRequest(@NotBlank String name, @NotBlank String username, @NotBlank String password) {

}

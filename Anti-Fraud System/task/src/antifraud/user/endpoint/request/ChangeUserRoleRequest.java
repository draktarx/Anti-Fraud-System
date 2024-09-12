package antifraud.user.endpoint.request;

import antifraud.user.model.AppUserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ChangeUserRoleRequest(@NotEmpty String username, @NotNull AppUserRole role) {

}

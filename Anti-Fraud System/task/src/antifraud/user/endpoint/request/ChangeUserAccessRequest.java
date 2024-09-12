package antifraud.user.endpoint.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ChangeUserAccessRequest(
        @NotEmpty String username, @NotNull OperationAccess operation) {

}

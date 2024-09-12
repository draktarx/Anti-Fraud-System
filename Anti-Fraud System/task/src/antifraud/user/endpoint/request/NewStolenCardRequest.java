package antifraud.user.endpoint.request;

import jakarta.validation.constraints.NotEmpty;

public record NewStolenCardRequest(@NotEmpty String number) {

}

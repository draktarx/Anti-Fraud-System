package antifraud.validator.endpoint.req;

import jakarta.validation.constraints.NotEmpty;

public record NewSuspiciousIpRequest(@NotEmpty String ip) {

}

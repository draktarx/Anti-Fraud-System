package antifraud.validator.endpoint.res;

import antifraud.validator.service.transactions.TransactionStatus;
import jakarta.validation.constraints.NotEmpty;

public record TransactionStatusInfo(
        @NotEmpty TransactionStatus status,
        @NotEmpty String info) {

}

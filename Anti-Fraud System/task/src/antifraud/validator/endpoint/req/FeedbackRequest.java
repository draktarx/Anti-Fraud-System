package antifraud.validator.endpoint.req;

import antifraud.validator.service.transactions.TransactionStatus;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequest(@NotNull Long transactionId, @NotNull TransactionStatus feedback) {

}

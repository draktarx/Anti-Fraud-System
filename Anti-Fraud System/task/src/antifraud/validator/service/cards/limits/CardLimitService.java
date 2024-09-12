package antifraud.validator.service.cards.limits;

import antifraud.validator.service.cards.limits.enums.CardLimitOperationType;
import antifraud.validator.service.cards.limits.enums.CardLimitType;
import jakarta.validation.constraints.NotNull;

public interface CardLimitService {

    void updateLimit(final String cardNumber,
                     final double transactionValue,
                     final CardLimitType cardLimitType,
                     final CardLimitOperationType operationType);

    Double fetchMaxAllowed(@NotNull String cardNumber);

    Double fetchMaxManual(@NotNull String cardNumber);

}

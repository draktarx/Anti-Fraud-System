package antifraud.validator.service.validations;

import antifraud.validator.service.validations.specific.*;
import org.springframework.stereotype.Component;

@Component
public class TransactionDataValidator implements Validator<TransactionValidation> {

    private final AmountValidator amountValidator;

    private final CardNumberValidator cardNumberValidator;

    private final IpValidator ipValidator;
    private final BlacklistValidator blacklistValidator;
    private final CorrelationValidator correlationValidator;

    public TransactionDataValidator(AmountValidator amountValidator,
                                    CardNumberValidator cardNumberValidator,
                                    IpValidator ipValidator,
                                    BlacklistValidator blacklistValidator,
                                    CorrelationValidator correlationValidator) {
        this.amountValidator = amountValidator;
        this.cardNumberValidator = cardNumberValidator;
        this.ipValidator = ipValidator;
        this.blacklistValidator = blacklistValidator;
        this.correlationValidator = correlationValidator;
    }

    @Override
    public void validate(TransactionValidation data) {
        cardNumberValidator.validate(data.getTxRequest().number());
        ipValidator.validate(data.getTxRequest().ip());
        blacklistValidator.validate(data);
        correlationValidator.validate(data);
        amountValidator.validate(data);
    }

}

package antifraud.validator.service.validations.specific;

import antifraud.validator.service.cards.limits.CardLimitServiceImpl;
import antifraud.validator.service.transactions.TransactionError;
import antifraud.validator.service.transactions.TransactionStatus;
import antifraud.validator.service.validations.TransactionValidation;
import antifraud.validator.service.validations.Validator;
import org.springframework.stereotype.Component;


@Component
public class AmountValidator implements Validator<TransactionValidation> {

    private final CardLimitServiceImpl cardLimitService;

    public AmountValidator(CardLimitServiceImpl cardLimitService) {
        this.cardLimitService = cardLimitService;
    }

    @Override
    public void validate(TransactionValidation data) {

        final Double MAX_ALLOWED_AMOUNT = cardLimitService.fetchMaxAllowed(data.getTxRequest().number());
        final Double MAX_ALLOWED_AMOUNT_FOR_MANUAL_PROCESSING = cardLimitService.fetchMaxManual(data.getTxRequest().number());
        final Long amount = data.getTxRequest().amount();

        switch (data.getTxValidationStatus().getStatus()) {
            case PROHIBITED -> {
                if (data.getTxRequest().amount() > MAX_ALLOWED_AMOUNT_FOR_MANUAL_PROCESSING) {
                    data.getTxValidationStatus().getInfo().add(TransactionError.AMOUNT.info());
                }
            }
            case MANUAL_PROCESSING -> {
                if (amount > MAX_ALLOWED_AMOUNT) {
                    data.getTxValidationStatus().getInfo().add(TransactionError.AMOUNT.info());
                }
            }
            case ALLOWED -> {
                data.getTxValidationStatus().getInfo().clear();
                if (amount <= MAX_ALLOWED_AMOUNT) {
                    data.getTxValidationStatus().getInfo().add(TransactionError.NONE.info());
                } else if (amount <= MAX_ALLOWED_AMOUNT_FOR_MANUAL_PROCESSING) {
                    data.getTxValidationStatus().setStatus(TransactionStatus.MANUAL_PROCESSING);
                    data.getTxValidationStatus().getInfo().add(TransactionError.AMOUNT.info());
                } else {
                    data.getTxValidationStatus().setStatus(TransactionStatus.PROHIBITED);
                    data.getTxValidationStatus().getInfo().add(TransactionError.AMOUNT.info());
                }
            }
        }
    }

}

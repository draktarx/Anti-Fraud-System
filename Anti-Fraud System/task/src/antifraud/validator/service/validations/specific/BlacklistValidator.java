package antifraud.validator.service.validations.specific;

import antifraud.validator.service.cards.stolen.StolenCardService;
import antifraud.validator.service.ips.SuspiciousIpService;
import antifraud.validator.service.transactions.TransactionError;
import antifraud.validator.service.transactions.TransactionStatus;
import antifraud.validator.service.validations.TransactionValidation;
import antifraud.validator.service.validations.Validator;
import org.springframework.stereotype.Component;

@Component
public class BlacklistValidator implements Validator<TransactionValidation> {

    private final SuspiciousIpService suspiciousIpService;

    private final StolenCardService stolenCardService;

    public BlacklistValidator(SuspiciousIpService suspiciousIpService,
                              StolenCardService stolenCardService) {
        this.suspiciousIpService = suspiciousIpService;
        this.stolenCardService = stolenCardService;
    }

    @Override
    public void validate(TransactionValidation data) {
        if (suspiciousIpService.isInTheBlackList(data.getTxRequest().ip())) {
            data.getTxValidationStatus().setStatus(TransactionStatus.PROHIBITED);
            data.getTxValidationStatus().getInfo().add(TransactionError.IP.info());
        }
        if (stolenCardService.isInTheBlackList(data.getTxRequest().number())) {
            data.getTxValidationStatus().setStatus(TransactionStatus.PROHIBITED);
            data.getTxValidationStatus().getInfo().add(TransactionError.CARD_NUMBER.info());
        }
    }

}

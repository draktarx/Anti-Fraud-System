package antifraud.validator.service.validations.specific;

import antifraud.validator.repository.TransactionRepository;
import antifraud.validator.service.transactions.TransactionError;
import antifraud.validator.service.transactions.TransactionStatus;
import antifraud.validator.service.validations.TransactionValidation;
import antifraud.validator.service.validations.Validator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CorrelationValidator implements Validator<TransactionValidation> {

    private final TransactionRepository repository;

    private static final String REGION_CORRELATION = TransactionError.REGION_CORRELATION.info();
    private static final String IP_CORRELATION = TransactionError.IP_CORRELATION.info();

    public CorrelationValidator(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(TransactionValidation data) {
        LocalDateTime oneHourAgo = data.getTxRequest().date().minusHours(1);
        prohibited(data, oneHourAgo);
        manual(data, oneHourAgo);
    }

    private void manual(TransactionValidation data, LocalDateTime oneHourAgo) {
        boolean hasRegionCorrelation = repository.existsTransactionsFromExactlyTwoDistinctRegions(
                data.getTxRequest().number(),
                oneHourAgo,
                data.getTxRequest().date(),
                data.getTxRequest().region()
        );
        if (hasRegionCorrelation) {
            data.getTxValidationStatus().getInfo().add(REGION_CORRELATION);
        }
        boolean hasIpCorrelation = repository.existsTransactionsFromExactlyTwoUniqueIps(
                data.getTxRequest().number(),
                oneHourAgo,
                data.getTxRequest().date(),
                data.getTxRequest().ip()
        );
        if (hasIpCorrelation) {
            data.getTxValidationStatus().getInfo().add(IP_CORRELATION);
        }
        if (hasRegionCorrelation || hasIpCorrelation) {
            data.getTxValidationStatus().setStatus(TransactionStatus.MANUAL_PROCESSING);
        }
    }

    private void prohibited(TransactionValidation data, LocalDateTime oneHourAgo) {
        boolean hasRegionCorrelation = repository.existsTransactionsFromMoreThanTwoRegions(
                data.getTxRequest().number(),
                oneHourAgo,
                data.getTxRequest().date(),
                data.getTxRequest().region()
        );
        if (hasRegionCorrelation) {
            data.getTxValidationStatus().getInfo().add(REGION_CORRELATION);
        }
        boolean hasIpCorrelation = repository.existsTransactionsFromMoreThanTwoUniqueIps(
                data.getTxRequest().number(),
                oneHourAgo,
                data.getTxRequest().date(),
                data.getTxRequest().ip()
        );
        if (hasIpCorrelation) {
            data.getTxValidationStatus().getInfo().add(IP_CORRELATION);
        }

        if (hasRegionCorrelation || hasIpCorrelation) {
            data.getTxValidationStatus().setStatus(TransactionStatus.PROHIBITED);
        }
    }

}

package antifraud.validator.service.feedback;

import antifraud.validator.endpoint.exception.feedback.FeedbackException;
import antifraud.validator.endpoint.req.FeedbackRequest;
import antifraud.validator.endpoint.req.NewTransactionRequest;
import antifraud.validator.model.Transaction;
import antifraud.validator.service.transactions.TransactionService;
import antifraud.validator.service.cards.limits.CardLimitService;
import antifraud.validator.service.cards.limits.enums.CardLimitOperationType;
import antifraud.validator.service.cards.limits.enums.CardLimitType;
import antifraud.validator.service.transactions.TransactionStatus;
import antifraud.validator.service.validations.TransactionDataValidator;
import antifraud.validator.service.validations.TransactionValidation;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final TransactionService transactionService;

    private final TransactionDataValidator transactionDataValidator;

    private final CardLimitService cardLimitService;

    public FeedbackServiceImpl(TransactionService transactionService,
                               TransactionDataValidator transactionDataValidator,
                               CardLimitService cardLimitService) {
        this.transactionService = transactionService;
        this.transactionDataValidator = transactionDataValidator;
        this.cardLimitService = cardLimitService;
    }

    @Override
    @Transactional
    public void updateTransactionFeedback(FeedbackRequest request) throws FeedbackException {
        Transaction transaction = transactionService.findById(request.transactionId());

        if (transaction.getFeedback() != null) {
            throw new EntityExistsException("Feedback already exists for this transaction");
        }

        try {
            //TransactionValidation tx = buildTx(transaction);
            //transactionDataValidator.validate(tx);

            Transaction tx = transactionService.findById(request.transactionId());
            final TransactionStatus validity = tx.getResult();
            final TransactionStatus feedback = request.feedback();

            if (feedback == validity) {
                throw new FeedbackException(
                        "Feedback status matches the original transaction status");
            }

            updateCardLimitBasedOnFeedback(validity,
                                           feedback,
                                           transaction.getNumber(),
                                           transaction.getAmount());

            transaction.setFeedback(feedback);
            transactionService.save(transaction);
        } catch (Exception e) {
            throw new FeedbackException(e.getMessage(), e);
        }
    }

    private static TransactionValidation buildTx(Transaction transaction) {
        NewTransactionRequest txRequest = new NewTransactionRequest(transaction.getAmount(),
                                                                    transaction.getIp(),
                                                                    transaction.getNumber(),
                                                                    transaction.getRegion(),
                                                                    transaction.getDate());
        TransactionValidation txValidation = new TransactionValidation(txRequest);
        return txValidation;
    }

    private void updateCardLimitBasedOnFeedback(TransactionStatus validity,
                                                TransactionStatus feedback,
                                                String cardNumber,
                                                Long transactionValue) {
        if (validity == TransactionStatus.ALLOWED) {
            handleAllowedValidty(cardNumber, feedback, transactionValue);
        } else if (validity == TransactionStatus.MANUAL_PROCESSING) {
            handleManualProcessingValidty(cardNumber, feedback, transactionValue);
        } else if (validity == TransactionStatus.PROHIBITED) {
            handleProhibitedValidty(cardNumber, feedback, transactionValue);
        }
    }

    private void handleAllowedValidty(String cardNumber,
                                      TransactionStatus feedback,
                                      Long transactionValue) {
        if (feedback == TransactionStatus.MANUAL_PROCESSING) {
            // decrease max allowed
            cardLimitService.updateLimit(cardNumber,
                                         transactionValue,
                                         CardLimitType.MAX_ALLOWED_AMOUNT,
                                         CardLimitOperationType.DECREASE);
        } else if (feedback == TransactionStatus.PROHIBITED) {
            // decrease max allowed and max manual
            cardLimitService.updateLimit(cardNumber,
                                         transactionValue,
                                         CardLimitType.MAX_ALLOWED_AMOUNT,
                                         CardLimitOperationType.DECREASE);
            cardLimitService.updateLimit(cardNumber,
                                         transactionValue,
                                         CardLimitType.MAX_ALLOWED_AMOUNT_FOR_MANUAL_PROCESSING,
                                         CardLimitOperationType.DECREASE);
        }
    }

    private void handleManualProcessingValidty(String cardNumber,
                                               TransactionStatus feedback,
                                               Long transactionValue) {
        if (feedback == TransactionStatus.ALLOWED) {
            // increase max allowed
            cardLimitService.updateLimit(cardNumber,
                                         transactionValue,
                                         CardLimitType.MAX_ALLOWED_AMOUNT,
                                         CardLimitOperationType.INCREASE);
        } else if (feedback == TransactionStatus.PROHIBITED) {
            // decrease max manual
            cardLimitService.updateLimit(cardNumber,
                                         transactionValue,
                                         CardLimitType.MAX_ALLOWED_AMOUNT_FOR_MANUAL_PROCESSING,
                                         CardLimitOperationType.DECREASE);
        }
    }

    private void handleProhibitedValidty(String cardNumber,
                                         TransactionStatus feedback,
                                         Long transactionValue) {
        if (feedback == TransactionStatus.ALLOWED) {
            // increase max allowed and max manual
            cardLimitService.updateLimit(cardNumber,
                                         transactionValue,
                                         CardLimitType.MAX_ALLOWED_AMOUNT,
                                         CardLimitOperationType.INCREASE);
            cardLimitService.updateLimit(cardNumber,
                                         transactionValue,
                                         CardLimitType.MAX_ALLOWED_AMOUNT_FOR_MANUAL_PROCESSING,
                                         CardLimitOperationType.INCREASE);
        } else if (feedback == TransactionStatus.MANUAL_PROCESSING) {
            // increase max manual
            cardLimitService.updateLimit(cardNumber,
                                         transactionValue,
                                         CardLimitType.MAX_ALLOWED_AMOUNT_FOR_MANUAL_PROCESSING,
                                         CardLimitOperationType.INCREASE);
        }
    }

}

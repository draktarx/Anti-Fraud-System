package antifraud.validator.service.cards.limits;

import antifraud.validator.model.CardLimit;
import antifraud.validator.repository.CardLimitRepository;
import antifraud.validator.service.cards.limits.enums.CardLimitOperationType;
import antifraud.validator.service.cards.limits.enums.CardLimitType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CardLimitServiceImpl implements CardLimitService {

    private static final double CURRENT_LIMIT_FACTOR = 0.8;

    private static final double TRANSACTION_LIMIT_FACTOR = 0.2;

    public static final double DEFAULT_MAX_ALLOWED = 200.0;

    public static final double DEFAULT_MAX_ALLOWED_MANUAL = 1500.0;

    private final CardLimitRepository repository;

    public CardLimitServiceImpl(CardLimitRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void updateLimit(final String cardNumber,
                            final double transactionValue,
                            final CardLimitType cardLimitType,
                            final CardLimitOperationType operationType) {
        CardLimit cardLimit = fetchCardLimit(cardNumber);

        switch (cardLimitType) {
            case MAX_ALLOWED_AMOUNT ->
                    cardLimit.setMaxAllowed(applyOperation(cardLimit.getMaxAllowed(),
                                                           transactionValue,
                                                           operationType));
            case MAX_ALLOWED_AMOUNT_FOR_MANUAL_PROCESSING -> cardLimit.setMaxManual(applyOperation(
                    cardLimit.getMaxManual(),
                    transactionValue,
                    operationType));
        }

        repository.save(cardLimit);
    }

    @Override
    public Double fetchMaxAllowed(String cardNumber) {
        return repository
                .findByNumber(cardNumber)
                .map(CardLimit::getMaxAllowed)
                .orElse(DEFAULT_MAX_ALLOWED);
    }

    @Override
    public Double fetchMaxManual(String cardNumber) {
        return repository
                .findByNumber(cardNumber)
                .map(CardLimit::getMaxManual)
                .orElse(DEFAULT_MAX_ALLOWED_MANUAL);
    }

    private CardLimit fetchCardLimit(final String cardNumber) {
        return repository.findByNumber(cardNumber).orElseGet(() -> {
            CardLimit newCardLimit = new CardLimit();
            newCardLimit.setNumber(cardNumber);
            newCardLimit.setMaxAllowed(DEFAULT_MAX_ALLOWED);
            newCardLimit.setMaxManual(DEFAULT_MAX_ALLOWED_MANUAL);
            return repository.save(newCardLimit);
        });
    }

    protected double applyOperation(final double currentLimit,
                                    final double transactionValue,
                                    final CardLimitOperationType operationType) {
        return switch (operationType) {
            case INCREASE -> increaseLimit(currentLimit, transactionValue);
            case DECREASE -> decreaseLimit(currentLimit, transactionValue);
        };
    }

    protected double increaseLimit(double currentLimit, double transactionValue) {
        return Math.ceil(CURRENT_LIMIT_FACTOR * currentLimit + TRANSACTION_LIMIT_FACTOR * transactionValue);
    }

    protected double decreaseLimit(double currentLimit, double transactionValue) {
        return Math.ceil(CURRENT_LIMIT_FACTOR * currentLimit - TRANSACTION_LIMIT_FACTOR * transactionValue);
    }

}

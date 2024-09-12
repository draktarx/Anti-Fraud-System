package antifraud.validator.service.cards.stolen;

import antifraud.validator.model.Card;
import antifraud.validator.repository.StolenCardRepository;
import antifraud.validator.service.validations.specific.CardNumberValidator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StolenCardServiceImpl implements StolenCardService {

    private final StolenCardRepository repository;

    private final CardNumberValidator validator;

    public StolenCardServiceImpl(StolenCardRepository repository, CardNumberValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public Card save(String number) {
        validator.validate(number);
        if (repository.existsByNumber(number)) {
            throw new EntityExistsException(String.format("Card with number %s already exists in the database.", number));
        }
        Card card = new Card();
        card.setNumber(number);
        return repository.save(card);
    }

    @Override
    @Transactional
    public List<Card> list() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    @Transactional
    public boolean isInTheBlackList(String cardNumber) {
        return repository.existsByNumber(cardNumber);
    }

    @Override
    @Transactional
    public void deleteByCardNumber(String cardNumber) {
        validator.validate(cardNumber);
        repository.findByNumber(cardNumber).ifPresentOrElse(repository::delete, () -> {
            throw new EntityNotFoundException(String.format("Card with number %s not found.", cardNumber));
        });
    }

}

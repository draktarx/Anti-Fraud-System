package antifraud.validator.service.cards.stolen;

import antifraud.validator.model.Card;

import java.util.List;

public interface StolenCardService {

    Card save(String number);

    List<Card> list();

    boolean isInTheBlackList(String cardNumber);

    void deleteByCardNumber(String number);

}

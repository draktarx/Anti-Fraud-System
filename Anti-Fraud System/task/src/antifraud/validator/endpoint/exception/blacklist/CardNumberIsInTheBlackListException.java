package antifraud.validator.endpoint.exception.blacklist;

public class CardNumberIsInTheBlackListException extends RuntimeException {

    public CardNumberIsInTheBlackListException(String message) {
        super(message);
    }

}

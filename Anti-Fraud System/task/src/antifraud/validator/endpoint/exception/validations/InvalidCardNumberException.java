package antifraud.validator.endpoint.exception.validations;

public class InvalidCardNumberException extends RuntimeException {

    public InvalidCardNumberException(String message) {
        super(message);
    }

}

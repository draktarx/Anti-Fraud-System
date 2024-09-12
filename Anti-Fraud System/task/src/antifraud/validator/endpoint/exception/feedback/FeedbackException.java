package antifraud.validator.endpoint.exception.feedback;

public class FeedbackException extends RuntimeException {

    public FeedbackException(String message) {
        super(message);
    }

    public FeedbackException(String message, Exception exception) {
        super(message, exception);
    }

}

package antifraud.validator.endpoint.exception.feedback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeedbackExceptionHandler {

    @ExceptionHandler(FeedbackException.class)
    public ResponseEntity<String> handleFeedbackException(FeedbackException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

}

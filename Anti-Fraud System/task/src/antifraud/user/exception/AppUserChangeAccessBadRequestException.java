package antifraud.user.exception;

import org.springframework.http.HttpStatus;

public class AppUserChangeAccessBadRequestException extends RuntimeException {

    private final HttpStatus status;

    public AppUserChangeAccessBadRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}

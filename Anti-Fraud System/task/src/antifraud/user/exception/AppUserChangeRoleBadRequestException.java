package antifraud.user.exception;

import org.springframework.http.HttpStatus;

public class AppUserChangeRoleBadRequestException extends RuntimeException {

    private final HttpStatus status;

    public AppUserChangeRoleBadRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}

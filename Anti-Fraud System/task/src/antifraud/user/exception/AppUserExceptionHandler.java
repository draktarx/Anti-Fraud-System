package antifraud.user.exception;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppUserExceptionHandler {

    @ExceptionHandler(AppUserAlreadyExists.class)
    public HttpEntity<String> handleUserAlreadyExistsException(AppUserAlreadyExists exception) {
        return ResponseEntity.status(409).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(400).body("bad request");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity.status(404).body("User not found: " + exception.getMessage());
    }

    @ExceptionHandler(AppUserChangeRoleBadRequestException.class)
    public ResponseEntity<String> handleChangeRoleException(AppUserChangeRoleBadRequestException exception) {
        return ResponseEntity.status(exception.getStatus()).body("bad request." + exception.getMessage());
    }


    @ExceptionHandler(AppUserChangeAccessBadRequestException.class)
    public ResponseEntity<String> handleChangeRoleException(AppUserChangeAccessBadRequestException exception) {
        return ResponseEntity.status(exception.getStatus()).body("bad request." + exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(400).body("bad request");
    }

}

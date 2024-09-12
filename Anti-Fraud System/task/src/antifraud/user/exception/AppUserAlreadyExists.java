package antifraud.user.exception;

public class AppUserAlreadyExists extends RuntimeException {

    public AppUserAlreadyExists(String message) {
        super(message);
    }

}

package antifraud.validator.endpoint.exception.blacklist;

public class IpIsInTheBlackListException extends RuntimeException {
  public IpIsInTheBlackListException(String message) {
    super(message);
  }
}

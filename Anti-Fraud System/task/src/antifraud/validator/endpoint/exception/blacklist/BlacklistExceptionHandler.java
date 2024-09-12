package antifraud.validator.endpoint.exception.blacklist;

import antifraud.validator.endpoint.res.TransactionInfoResponse;
import antifraud.validator.service.transactions.TransactionStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BlacklistExceptionHandler {

    @ExceptionHandler(IpIsInTheBlackListException.class)
    public ResponseEntity<TransactionInfoResponse> handleIpIsInTheBlackListException(
            IpIsInTheBlackListException ex) {
        var response = new TransactionInfoResponse(TransactionStatus.PROHIBITED.toString(), "ip");
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(CardNumberIsInTheBlackListException.class)
    public ResponseEntity<TransactionInfoResponse> handleCardNumberIsInTheBlackListException(
            CardNumberIsInTheBlackListException ex) {
        var response = new TransactionInfoResponse(TransactionStatus.PROHIBITED.toString(),
                                                   "card-number");
        return ResponseEntity.ok().body(response);
    }

}

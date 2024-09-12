package antifraud.validator.endpoint.res;

import antifraud.validator.model.enums.Region;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public record TransactionDto(
        Long transactionId,
        Long amount,
        String ip,
        String number,
        Region region,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date,
        String result,
        String feedback) {

}

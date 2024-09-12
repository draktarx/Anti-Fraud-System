package antifraud.validator.endpoint.req;

import antifraud.validator.model.enums.Region;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record NewTransactionRequest(
        @Positive Long amount,
        @NotNull String ip,
        @NotNull String number,
        @NotNull Region region,
        @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date) {

}

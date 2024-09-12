package antifraud.validator.model;

import antifraud.validator.model.enums.Region;
import antifraud.validator.service.transactions.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Positive
    private Long amount;

    @NotNull
    private String ip;

    @NotNull
    private String number;

    @NotNull
    private Region region;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private TransactionStatus result;

    private TransactionStatus feedback;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public @Positive Long getAmount() {
        return amount;
    }

    public void setAmount(@Positive Long amount) {
        this.amount = amount;
    }

    public @NotNull String getIp() {
        return ip;
    }

    public void setIp(@NotNull String ip) {
        this.ip = ip;
    }

    public @NotNull String getNumber() {
        return number;
    }

    public void setNumber(@NotNull String number) {
        this.number = number;
    }

    public @NotNull Region getRegion() {
        return region;
    }

    public void setRegion(@NotNull Region region) {
        this.region = region;
    }

    public @NotNull LocalDateTime getDate() {
        return date;
    }

    public void setDate(@NotNull LocalDateTime date) {
        this.date = date;
    }

    public TransactionStatus getResult() {
        return result;
    }

    public void setResult(TransactionStatus result) {
        this.result = result;
    }

    public TransactionStatus getFeedback() {
        return feedback;
    }

    public void setFeedback(TransactionStatus feedback) {
        this.feedback = feedback;
    }

}

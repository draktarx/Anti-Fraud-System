package antifraud.validator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class CardLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double maxAllowed; // Maximum ALLOWED limit

    @NotNull
    private Double maxManual; // Maximum MANUAL_PROCESSING limit

    @NotNull
    private String number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Double getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(@NotNull Double maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public @NotNull Double getMaxManual() {
        return maxManual;
    }

    public void setMaxManual(@NotNull Double maxManual) {
        this.maxManual = maxManual;
    }

    public @NotNull String getNumber() {
        return number;
    }

    public void setNumber(@NotNull String number) {
        this.number = number;
    }

}

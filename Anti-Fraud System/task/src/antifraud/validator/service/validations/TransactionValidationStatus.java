package antifraud.validator.service.validations;

import antifraud.validator.service.transactions.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

public class TransactionValidationStatus {

    private TransactionStatus status = TransactionStatus.ALLOWED;

    private List<String> info = new ArrayList<>();

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public List<String> getInfo() {
        info.sort(String::compareToIgnoreCase);
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }

}

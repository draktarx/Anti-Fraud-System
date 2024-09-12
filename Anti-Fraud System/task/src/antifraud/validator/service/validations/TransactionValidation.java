package antifraud.validator.service.validations;

import antifraud.validator.endpoint.req.NewTransactionRequest;

public class TransactionValidation {

    private final NewTransactionRequest txRequest;

    private TransactionValidationStatus txValidationStatus;

    public TransactionValidation(NewTransactionRequest txRequest) {
        this.txRequest = txRequest;
        this.txValidationStatus = new TransactionValidationStatus();
    }

    public NewTransactionRequest getTxRequest() {
        return txRequest;
    }

    public TransactionValidationStatus getTxValidationStatus() {
        return txValidationStatus;
    }

    public void setTxValidationStatus(TransactionValidationStatus txValidationStatus) {
        this.txValidationStatus = txValidationStatus;
    }

}

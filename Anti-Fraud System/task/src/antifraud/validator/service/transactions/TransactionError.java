package antifraud.validator.service.transactions;

public enum TransactionError {
    NONE("none"),
    AMOUNT("amount"),
    IP("ip"),
    IP_CORRELATION("ip-correlation"),
    REGION_CORRELATION("region-correlation"),
    CARD_NUMBER("card-number");

    private final String info;

    // Constructor
    TransactionError(String info) {
        this.info = info;
    }

    // Method to get info
    public String info() {
        return this.info;
    }
}

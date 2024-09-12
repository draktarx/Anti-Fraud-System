package antifraud.validator.endpoint.res;

import antifraud.validator.model.Transaction;

public class TransactionMapper {

    public static TransactionDto mapToDto(Transaction transaction) {
        return new TransactionDto(transaction.getTransactionId(),
                                  transaction.getAmount(),
                                  transaction.getIp(),
                                  transaction.getNumber(),
                                  transaction.getRegion(),
                                  transaction.getDate(),
                                  transaction.getResult() != null ? transaction
                                          .getResult()
                                          .toString() : "",
                                  transaction.getFeedback() != null ? transaction
                                          .getFeedback()
                                          .toString() : "");
    }

}

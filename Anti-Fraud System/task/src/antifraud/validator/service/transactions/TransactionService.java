package antifraud.validator.service.transactions;

import antifraud.validator.model.Transaction;
import antifraud.validator.repository.TransactionRepository;
import antifraud.validator.service.validations.TransactionValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
        //this.repository.deleteAll();
    }

    public List<Transaction> list() {
        return repository.findAllTransactionsSortedByIdAsc();
    }

    public List<Transaction> listByNumber(String number) {
        if (!repository.existsByNumber(number)) {
            throw new EntityNotFoundException(String.format("Card with number %s not found.",
                                                            number));
        }
        return repository.findAllTransactionsByNumberSortedByIdAsc(number);
    }

    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    public Transaction findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
    }

    @Transactional
    public Transaction saveTransactionRequest(TransactionValidation tx) {
        var transaction = new Transaction();
        transaction.setAmount(tx.getTxRequest().amount());
        transaction.setIp(tx.getTxRequest().ip());
        transaction.setNumber(tx.getTxRequest().number());
        transaction.setRegion(tx.getTxRequest().region());
        transaction.setDate(tx.getTxRequest().date());
        transaction.setResult(tx.getTxValidationStatus().getStatus());
        return repository.save(transaction);
    }

}

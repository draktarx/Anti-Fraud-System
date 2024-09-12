package antifraud.validator.endpoint;

import antifraud.user.service.AppUserService;
import antifraud.validator.endpoint.req.FeedbackRequest;
import antifraud.validator.endpoint.req.NewTransactionRequest;
import antifraud.validator.endpoint.res.TransactionDto;
import antifraud.validator.endpoint.res.TransactionMapper;
import antifraud.validator.model.Transaction;
import antifraud.validator.service.transactions.TransactionService;
import antifraud.validator.service.feedback.FeedbackService;
import antifraud.validator.service.validations.TransactionDataValidator;
import antifraud.validator.service.validations.TransactionValidation;
import antifraud.validator.service.validations.TransactionValidationStatus;
import antifraud.validator.service.validations.specific.CardNumberValidator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
public class TransactionsController {

    private final TransactionDataValidator transactionDataValidator;

    private final AppUserService appUserService;

    private final TransactionService transactionService;

    private final FeedbackService feedbackService;

    private final CardNumberValidator cardNumberValidator;

    public TransactionsController(TransactionDataValidator transactionDataValidator,
                                  AppUserService appUserService,
                                  TransactionService transactionService,
                                  FeedbackService feedbackService,
                                  CardNumberValidator cardNumberValidator) {
        this.transactionDataValidator = transactionDataValidator;
        this.appUserService = appUserService;
        this.transactionService = transactionService;
        this.feedbackService = feedbackService;
        this.cardNumberValidator = cardNumberValidator;
    }

    @PostMapping("/transaction")
    public ResponseEntity<Object> handleTransaction(
            @Valid @RequestBody NewTransactionRequest request, Authentication authentication) {
        try {

            String username = authentication.getName(); // Store authenticated user info
            var isLocked = appUserService.userIsLocked(username);
            if (isLocked) {
                return ResponseEntity.status(401).build();
            }
            //transactionService.saveTransactionRequest(request);
            TransactionValidation transactionValidation = new TransactionValidation(request);
            transactionDataValidator.validate(transactionValidation);
            var result = transactionValidation.getTxValidationStatus();
            transactionService.saveTransactionRequest(transactionValidation);

            HashMap<String, String> response = new HashMap<>();
            response.put("result", result.getStatus().toString());
            response.put("info", String.join(", ", result.getInfo()));
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/transaction")
    public ResponseEntity<Object> updateFeedback(@Valid @RequestBody FeedbackRequest request) {
        feedbackService.updateTransactionFeedback(request);
        var response = transactionService.findById(request.transactionId());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionDto>> listTransactions() {
        var list = transactionService.list();
        var response = list.stream().map(TransactionMapper::mapToDto).toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/history/{number}")
    public ResponseEntity<List<TransactionDto>> listTransactionsByNumber(@PathVariable String number) {
        cardNumberValidator.validate(number);
        var list = transactionService.listByNumber(number);
        var response = list.stream().map(TransactionMapper::mapToDto).toList();
        return ResponseEntity.ok().body(response);
    }
}

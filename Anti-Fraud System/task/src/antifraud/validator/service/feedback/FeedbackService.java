package antifraud.validator.service.feedback;

import antifraud.validator.endpoint.req.FeedbackRequest;
import antifraud.validator.endpoint.exception.feedback.FeedbackException;

public interface FeedbackService {

    public void updateTransactionFeedback(FeedbackRequest request) throws FeedbackException;
}

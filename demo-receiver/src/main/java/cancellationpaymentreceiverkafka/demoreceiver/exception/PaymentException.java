package cancellationpaymentreceiverkafka.demoreceiver.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String message, Exception e) {
        super(message,e);
    }
}

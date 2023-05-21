package cancellationpaymentkafka.demo.exceptions;

public class UnsupportedPaymentException extends Throwable {
    public UnsupportedPaymentException(String message) {
        super(message);
    }
}

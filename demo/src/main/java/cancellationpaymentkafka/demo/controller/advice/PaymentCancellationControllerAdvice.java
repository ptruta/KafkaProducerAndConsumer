package cancellationpaymentkafka.demo.controller.advice;

import cancellationpaymentkafka.demo.controller.PaymentCancellationController;
import cancellationpaymentkafka.demo.exceptions.UnsupportedPaymentException;
import cancellationpaymentkafka.demo.model.error.PaymentErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice(basePackageClasses = PaymentCancellationController.class)
@RequiredArgsConstructor
public class PaymentCancellationControllerAdvice {

    private static final String ERROR_CODE_001 = "ERR-001";

    @ExceptionHandler({UnsupportedPaymentException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<PaymentErrorResponse> processUnsupportedPaymentException(Exception ex) {
        log.warn("Exception ({}) while handling request: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST)
                .body(PaymentErrorResponse.builder()
                        .errorMessage(
                                String.format("Error occurred. Service cannot be found: %s",
                                        ex.getMessage()))
                        .errorCode(ERROR_CODE_001)
                        .build());
    }
}

package cancellationpaymentkafka.demo.controller;


import cancellationpaymentkafka.demo.exceptions.UnsupportedPaymentException;
import cancellationpaymentkafka.demo.model.Payment;
import cancellationpaymentkafka.demo.service.PaymentCancellationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentCancellationController {

    private final PaymentCancellationService paymentCancellationService;

    @PostMapping(value = {"/payment-cancellation"})
    public ResponseEntity<Void> paymentCancellation(
            final @RequestBody Payment payment) throws UnsupportedPaymentException {

        log.info("Received payment {}",
                payment.getPayload());

        Payment paymentSupported =
                paymentCancellationService.isSupported((payment));
        if (paymentSupported == null) {
            throw new UnsupportedPaymentException(
                    String.format("The payment is not supported %s",
                            payment));
        }

        paymentCancellationService.handle(payment);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

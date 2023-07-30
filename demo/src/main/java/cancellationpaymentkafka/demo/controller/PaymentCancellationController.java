package cancellationpaymentkafka.demo.controller;


import cancellationpaymentkafka.demo.exceptions.UnsupportedPaymentException;
import cancellationpaymentkafka.demo.model.Payment;
import cancellationpaymentkafka.demo.service.PaymentCancellationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentCancellationController {

    private final PaymentCancellationService paymentCancellationService;

    @PostMapping(value = {"/payment-cancellation"})
    public ResponseEntity<Void> paymentCancellation(
            final @RequestBody @NotNull List<Payment> payments) throws UnsupportedPaymentException {

        payments.forEach(
                payment -> {
                    log.info("Received payment {}",
                            payment.getPayload());

                    Payment paymentSupported =
                            paymentCancellationService.isSupported((payment));
                    if (paymentSupported == null) {
                        try {
                            throw new UnsupportedPaymentException(
                                    String.format("The payment is not supported %s",
                                            payment));
                        } catch (UnsupportedPaymentException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    paymentCancellationService.handle(payment);
                }
        );

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

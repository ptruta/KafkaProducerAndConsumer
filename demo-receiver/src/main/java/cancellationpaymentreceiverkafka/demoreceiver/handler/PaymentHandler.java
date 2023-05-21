package cancellationpaymentreceiverkafka.demoreceiver.handler;

import cancellationpaymentreceiverkafka.demoreceiver.model.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentHandler {

    private final List<PaymentCallback> swiftCallbacks;

    private final ObjectMapper objectMapper;

    public void handle(String payload) {
        Payment payment = objectMapper.convertValue(payload, Payment.class);
        swiftCallbacks.stream()
                .filter(c -> c.accept(payment))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(String.format("Unknown message, will be discarded: %s",
                                payment)))
                .process(payment);
    }
}
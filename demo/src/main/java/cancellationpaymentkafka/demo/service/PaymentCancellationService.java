package cancellationpaymentkafka.demo.service;

import cancellationpaymentkafka.demo.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static cancellationpaymentkafka.demo.util.constants.PaymentConstants.OUTPUT_PAYMENT_OUT_0;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCancellationService {

    private final StreamBridge streamBridge;

    public Payment isSupported(Payment payment) {
        log.info("Check if payment type {} is supported ",
                payment.getType());

        return "C".equals(payment.getType())
                ? payment
                : null;
    }

    public void handle(Payment payment) {
        streamBridge.send(OUTPUT_PAYMENT_OUT_0, MessageBuilder.withPayload(payment)
                .setHeader(CONTENT_TYPE, APPLICATION_XML_VALUE)
                .build());
    }
}

package cancellationpaymentreceiverkafka.demoreceiver.handler;

import cancellationpaymentreceiverkafka.demoreceiver.model.Payment;

public interface PaymentCallback {

    boolean accept(Payment payment);

    void process(Payment payment);
}

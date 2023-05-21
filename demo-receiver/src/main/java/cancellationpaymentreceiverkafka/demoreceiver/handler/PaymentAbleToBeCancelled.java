package cancellationpaymentreceiverkafka.demoreceiver.handler;

import cancellationpaymentreceiverkafka.demoreceiver.model.Payment;

public class PaymentAbleToBeCancelled implements PaymentCallback {
    @Override
    public boolean accept(Payment payment) {
        return "C".equals(payment.getType());
    }

    @Override
    public void process(Payment payment) {
        if ("C".equals(payment.getType()) && payment.isActive()) {
            payment.setActive(false);
        }
    }
}

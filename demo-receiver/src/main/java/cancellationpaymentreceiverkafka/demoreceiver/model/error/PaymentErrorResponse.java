package cancellationpaymentreceiverkafka.demoreceiver.model.error;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
public class PaymentErrorResponse {

    private final String errorCode;
    private final String errorMessage;
}

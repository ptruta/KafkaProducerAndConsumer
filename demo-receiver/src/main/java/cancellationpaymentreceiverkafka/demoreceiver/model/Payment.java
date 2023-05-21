package cancellationpaymentreceiverkafka.demoreceiver.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private String type;
    private String amount;
    private String payload;
    private boolean isActive;
}

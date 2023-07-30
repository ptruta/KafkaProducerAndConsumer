//package cancellationpaymentreceiverkafka.demoreceiver.config;
//
//import cancellationpaymentreceiverkafka.demoreceiver.exception.PaymentException;
//import cancellationpaymentreceiverkafka.demoreceiver.handler.PaymentHandler;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//
//import java.util.function.Consumer;
//
//@Slf4j
//@Configuration
//@AllArgsConstructor
//public class MessagingConfig {
//
//    private final PaymentHandler paymentHandler;
//
//    @Bean
//    public Consumer<Message<String>> inputPayment() {
//        log.info("PaymentHandler {}", paymentHandler);
//        return message -> {
//            try {
//                paymentHandler.handle(message.getPayload());
//            } catch (Exception e) {
//                log.error(String.format("Exception thrown in handle" +
//                        " method from PaymentHandler class [%s]", e.getMessage()), e);
//                throw new PaymentException("Cannot process swift input ", e);
//            }
//        };
//    }
//
//}

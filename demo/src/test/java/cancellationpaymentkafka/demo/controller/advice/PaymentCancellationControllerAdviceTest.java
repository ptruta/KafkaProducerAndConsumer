package cancellationpaymentkafka.demo.controller.advice;

import cancellationpaymentkafka.demo.model.error.PaymentErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentCancellationControllerAdviceTest {

    public static final String GENERAL_EXCEPTION_MESSAGE = "general exception";
    public static final String ERROR_CODE_001 = "ERR-001";

    @InjectMocks
    private PaymentCancellationControllerAdvice controllerAdvice;

    @Test
    void shouldReturn404ForUnsupportedPaymentException() {
        //given
        Exception exception = new Exception(GENERAL_EXCEPTION_MESSAGE);

        // when
        final ResponseEntity<PaymentErrorResponse> response = controllerAdvice
                .processUnsupportedPaymentException(exception);

        // then
        assertEquals(ERROR_CODE_001, Objects.requireNonNull(response.getBody()).getErrorCode());
    }
}
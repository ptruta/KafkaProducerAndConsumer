package cancellationpaymentkafka.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.kafka.producer")
@Data
public class KafkaProducerConfig {

    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
}

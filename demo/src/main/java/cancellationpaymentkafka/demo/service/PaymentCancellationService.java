package cancellationpaymentkafka.demo.service;

import cancellationpaymentkafka.demo.config.KafkaProducerConfig;
import cancellationpaymentkafka.demo.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCancellationService {

//    private final StreamBridge streamBridge;

    private final KafkaProducerConfig kafkaProducerConfig;

    public Payment isSupported(Payment payment) {
        log.info("Check if payment type {} is supported ",
                payment.getType());

        return "C".equals(payment.getType())
                ? payment
                : null;
    }

    public void handle(Payment payment) {
        // Configurarea proprietăților producătorului
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfig.getBootstrapServers()); // Adresa brokerului Kafka
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getKeySerializer());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getValueSerializer());

        // Crearea producătorului Kafka
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Publicarea unui mesaj în topicul "mesaje"
        String topic = "payment_2023";
        String mesaj = payment.toString();
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, mesaj);
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Mesaj publicat cu succes în topic: " + metadata.topic());
                System.out.println("Partiția: " + metadata.partition());
                System.out.println("Offset: " + metadata.offset());
            } else {
                System.err.println("Eroare la publicarea mesajului: " + exception.getMessage());
            }
        });

        // Închiderea producătorului
//        producer.close();

//        streamBridge.send(INPUT_PAYMENT_OUT_0, MessageBuilder.withPayload(payment)
//                .setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//                .build());
    }
}

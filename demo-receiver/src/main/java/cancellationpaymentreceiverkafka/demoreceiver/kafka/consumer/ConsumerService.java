package cancellationpaymentreceiverkafka.demoreceiver.kafka.consumer;

import cancellationpaymentreceiverkafka.demoreceiver.config.KafkaConsumerConfig;
import cancellationpaymentreceiverkafka.demoreceiver.config.KafkaProducerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaProducerConfig kafkaProducerConfig;

    @KafkaListener(topics = "payment_2023")
    public void consumeMessages() {
        // Configurarea proprietăților consumatorului
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerConfig.getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfig.getGroupId()); // Numele grupului de consumatori
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfig.getKeyDeserializer());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfig.getValueDeserializer());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfig.getAutoOffsetReset());

        Properties propertiesProducer = new Properties();
        propertiesProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfig.getBootstrapServers());
        propertiesProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getKeySerializer());
        propertiesProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getKeySerializer());

//         Crearea consumatorului Kafka
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Abonarea la topicul dorit (de exemplu, "payment_2023")
        String topic = "payment_2023";
        consumer.subscribe(Collections.singleton(topic));

//        // Începerea buclei de consum pentru a prelua și procesa mesajele
//        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//            records.forEach(record -> {
//                System.out.println("Mesaj consumat: " + record.value());
//                // Implementați logica de procesare a mesajului aici
//            });
//        }
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
//        String topic = "payment_2023";
//
        KafkaProducer<String, String> producer = new KafkaProducer<>(propertiesProducer);
//
//        consumer.subscribe(Collections.singletonList(topic));
//
        int mesajeDeConsumat = 10;
        int totalMesajeConsumate = 0;
//
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // wait period

//            if (records.isEmpty()) {
//                continue;
//            }

            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Mesaj consumat: " + record.value());
                totalMesajeConsumate++;

                try {
                    // Procesarea mesajului
                    processMessage(record.value());
                } catch (Exception e) {
                    System.err.println("Eroare la procesarea mesajului: " + e.getMessage());
                    // În cazul unei erori, adăugăm mesajul în topicul de eroare
                    producer.send(new ProducerRecord<>("mesaje-eroare", record.value()), (errorMetadata, errorException) -> {
                        if (errorException == null) {
                            System.out.println("Mesajul de eroare trimis: " + record.value());
                        } else {
                            System.err.println("Eroare la trimiterea mesajului de eroare: " + errorException.getMessage());
                        }
                    });
                }

                if (totalMesajeConsumate >= mesajeDeConsumat) {
                    break;
                }
            }

            if (totalMesajeConsumate >= mesajeDeConsumat) {
                break;
            }
        }

//        consumer.close();
    }

    // Metoda de procesare a mesajului (doar un exemplu)
    private static void processMessage(String message) {
        // Simulăm o eroare pentru mesajele cu numere pare
        int numarMesaj = Integer.parseInt(message.split(" ")[1]);
        if (numarMesaj % 2 == 0) {
            throw new RuntimeException("Eroare la procesare");
        }
        // Procesare normală dacă nu există erori
        System.out.println("Procesare mesaj: " + message);
    }

}

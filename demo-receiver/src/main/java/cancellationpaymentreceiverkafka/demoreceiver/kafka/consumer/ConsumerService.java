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
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaProducerConfig kafkaProducerConfig;
    private static Queue<ConsumerRecord<String, String>> queueInProgressCardRequests;
    private static Queue<ConsumerRecord<String, String>> queueCardToProcessed;

    @KafkaListener(topics = {"payment_2023", "payment_2023_in_progress"})
    public void consumeMessages() throws InterruptedException {
        // Configurarea proprietăților consumatorului
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerConfig.getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfig.getGroupId()); // Numele grupului de consumatori
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfig.getKeyDeserializer());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfig.getValueDeserializer());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfig.getAutoOffsetReset());

        Properties propertiesProducer = new Properties();

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        String toProcessCardTopic = "payment_2023";
        String inProgressRequestsTopic = "payment_2023_in_progress";

        consumer.subscribe(Collections.singleton(toProcessCardTopic));

        int maxConcurrentMessages = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(maxConcurrentMessages);

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                final String[] message = {record.value()};
                System.out.println("Received message from to process topic: " + message[0]);

                // Process each message using a separate thread from the thread pool
                executorService.execute(() -> {
                    // Simulate message processing delay
                    try {
                        Thread.sleep(1000);

                        System.out.println(" Send the processed message to in progress topic: " + message[0]);

                        processeMessage(message[0]);

                        addToInProgressTopic(propertiesProducer, inProgressRequestsTopic, message[0]);
                    } catch (InterruptedException | SvboException e) {
                        System.out.println(" Send the processed message to process topic because of error: " + message[0]);
                        message[0] = message[0].replace("error", "nada");
                        addToProcessedTopic(propertiesProducer, toProcessCardTopic, message[0]);
                    }

                });
            }

            // Commit the offsets after processing the batch of records
            consumer.commitSync();
        }

    }

    private void processeMessage(String message) {
        // call to svbo
        // simulate exception
        if (message.contains("error")) {
            throw new SvboException("eroare la procesare svbo");
        }
    }

    private void addToInProgressTopic(Properties propertiesProducer, String inProgressRequestsTopic, String message) {
        // Send the processed message to the "in_progress_topic"
        ProducerRecord<String, String> processedRecord = new ProducerRecord<>(inProgressRequestsTopic, message);
        KafkaProducer<String, String> producer = createProducer(propertiesProducer);
        producer.send(processedRecord);
        producer.close();
    }

    private void addToProcessedTopic(Properties propertiesProducer, String processedTopic, String message) {
        // Send the processed message to the "processed topic"
        ProducerRecord<String, String> processedRecord = new ProducerRecord<>(processedTopic, message);
        KafkaProducer<String, String> producer = createProducer(propertiesProducer);
        producer.send(processedRecord);
        producer.close();
    }

    private KafkaProducer<String, String> createProducer(Properties propertiesProducer) {
        propertiesProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfig.getBootstrapServers());
        propertiesProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getKeySerializer());
        propertiesProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getKeySerializer());

        return new KafkaProducer<>(propertiesProducer);
    }
}

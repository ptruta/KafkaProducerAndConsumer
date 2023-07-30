## Java 8 & Spring Boot Microservices 
### Messaging distribution communication
#### StreamBridge from spring cloud
 * Spring Boot **VERSION** - 2.4.5

* In the version Spring Boot 2.4.5, library that is part of Spring Cloud 
and includes StreamBridge is called spring-cloud-stream. It provides 
support for developing message-based applications in a distributed 
environment. StreamBridge is a component of spring-cloud-stream and 
provides a simple way to produce and consume messages in a transparent way.

* To use StreamBridge in your project, you need to add the appropriate 
dependency to your project's pom.xml or build.gradle file. 
* Here is an example for Maven:


 ```xml
<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-stream</artifactId>
<version>3.1.3</version>
</dependency>
 ```

* Here is an example for Gradle:

 ```gradle
 implementation 'org.springframework.cloud:spring-cloud-stream:3.1.3'
 ```

* Make sure the specified version (3.1.3 in this example) 
is compatible with the Spring Boot 2.4.5 version you are using. 
Other dependencies associated with spring-cloud-stream may 
be required depending on your specific setup and needs.

* After adding the dependency, you can use StreamBridge in 
your code to produce and consume messages using the Spring 
Cloud Stream API. For more information on how to use StreamBridge 
and configure your Spring Cloud Stream-based application, 
I recommend consulting the official Spring Cloud documentation.

### Stream Bridge without using Kafka Cloud


* To use StreamBridge without using Kafka Cloud, you can set up
and configure a local Kafka server or a Kafka cluster managed by
your own infrastructure. Here are the main steps you can follow:

* Install and configure on-premises Kafka or a managed Kafka cluster.

* There are several options available for this, such as Apache Kafka,
Confluent Platform, or other Kafka service providers.

* Make sure you have the right version of the Kafka library installed
for the programming language you are using.

* StreamBridge has support for several languages, including Java, Python, and Go.

* In your code, add the dependencies needed to use StreamBridge.
These should include the StreamBridge library for your programming
language and any other dependencies needed to connect to your local
or managed Kafka cluster.

* Configure and initialize a StreamBridge producer to publish messages
to Kafka. Be sure to configure the appropriate local or managed Kafka
broker address and port settings.

* Configure and initialize a StreamBridge consumer to consume
messages from Kafka. Be sure to configure the appropriate
local or managed Kafka broker address and port settings.

* Use the methods and functions provided by StreamBridge to publish
and consume messages in Kafka. You can use the same methods and
functions as you would when using Kafka Cloud.

* It is important to consult the documentation and usage examples
specific to the programming language you are using, as the implementation
may vary depending on the library and framework you choose.

* Note that StreamBridge is a simple library that provides a higher
level of abstraction to work with Kafka. 

* If you want more granular control over Kafka configurations and functionality, you may need
to directly use the Kafka library corresponding to your programming
language or the APIs provided by the Kafka service provider you are using.


Link to configuration Kafka on MACOS:
https://www.conduktor.io/kafka/how-to-install-apache-kafka-on-mac/

Run this in shell terminal: /Users/patigeorgiana/Downloads/kafka_2.12-3.3.1/bin/zookeeper-server-start.sh /Users/patigeorgiana/Downloads/kafka_2.12-3.3.1/config/zookeeper.properties
Run this in another terminal: /Users/patigeorgiana/Downloads/kafka_2.12-3.3.1/bin/kafka-server-start.sh  /Users/patigeorgiana/Downloads/kafka_2.12-3.3.1/config/server.properties

./Users/patigeorgiana/Downloads/kafka_2.12-3.3.1/bin/kafka-topics.sh --create --topic payment_2023 --bootstrap-server localhost:9092 --partitions 3 --replication-factor 2


Idei de implementare:

Pentru a crea două microservicii Java care să comunice cu Kafka, puteți utiliza Kafka Topics pentru a asigura comunicarea între ele. Kafka Topics sunt folosite pentru a organiza mesajele și pentru a le distribui între producători și consumatori.

Pentru a crea un sistem în care producătorul pune mesajele într-o coadă, iar consumatorul poate face dequeue doar la câte vrea, puteți urma acești pași:

1. Creați două Kafka Topics: Unul pentru coada de mesaje și unul pentru consumator.

2. Microserviciul producător va publica mesajele în Kafka Topicul corespunzător pentru coadă.

3. Microserviciul consumator va consuma mesajele din Kafka Topicul corespunzător pentru consum.

4. În implementarea consumatorului, puteți decide câte mesaje să consumați la fiecare apel sau la fiecare rulare a consumatorului. De asemenea, puteți implementa logica pentru a controla când și câte mesaje sunt preluate din coadă.

5. Asigurați-vă că setați configurările corespunzătoare pentru mesajele din topicurile Kafka, cum ar fi factorul de replicare și retentia.

Nu aveți nevoie de Kafka Tables pentru a realiza acest lucru. Kafka Tables sunt folosite pentru a realiza analize complexe asupra fluxurilor de date și nu ar fi necesare în scenariul descris de dvs.

Este important să luați în considerare faptul că Kafka asigură persistența mesajelor pentru o perioadă de timp specificată (retentia) și permite consumatorilor să preia mesajele pe măsură ce ele sunt publicate. Dacă doriți să controlați strict numărul de mesaje preluate de consumator, ar trebui să implementați această logica în consumator.

În general, utilizarea Kafka pentru a implementa un sistem de cozi și mesaje este potrivită pentru scenarii distribuite și tolerante la erori. Cu toate acestea, este important să luați în considerare și alți factori, cum ar fi scalabilitatea și performanța sistemului, în funcție de cerințele specifice ale aplicației dvs.

Desigur! Voi oferi un exemplu simplu pentru a ilustra cum puteți folosi Kafka pentru a implementa un sistem cu un producător care pune mesajele într-o coadă și un consumator care face dequeue doar la câte vrea.

Presupunem că avem două microservicii: `ProducerService` și `ConsumerService`. Pentru a folosi Kafka, vom avea nevoie de biblioteca Kafka pentru Java, deci asigurați-vă că o aveți în proiectul dvs.

1. Creați două Kafka Topics: "mesaje" pentru coadă și "mesaje-consumate" pentru consumator.

2. ProducerService - producătorul de mesaje:

```java
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class ProducerService {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "mesaje";

        for (int i = 0; i < 10; i++) {
            String message = "Mesajul " + i;
            producer.send(new ProducerRecord<>(topic, message), (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Mesajul trimis: " + message);
                } else {
                    System.err.println("Eroare la trimiterea mesajului: " + exception.getMessage());
                }
            });
        }

        producer.close();
    }
}
```

3. ConsumerService - consumatorul de mesaje:

```java
import org.apache.kafka.clients.consumer.*;
import java.util.Collections;
import java.util.Properties;

public class ConsumerService {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "grup-consumatori");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        String topic = "mesaje";

        consumer.subscribe(Collections.singletonList(topic));

        int mesajeDorite = 5;
        int mesajeConsumate = 0;

        while (mesajeConsumate < mesajeDorite) {
            ConsumerRecords<String, String> records = consumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Mesaj consumat: " + record.value());
                mesajeConsumate++;

                if (mesajeConsumate >= mesajeDorite) {
                    break;
                }
            }
        }

        consumer.close();
    }
}
```

În acest exemplu, `ProducerService` va publica 10 mesaje în Kafka Topicul "mesaje". Apoi, `ConsumerService` va prelua doar primele 5 mesaje disponibile din acest topic și le va afișa. Mesajele rămase din "mesaje" nu vor fi consumate, ci vor fi păstrate în Kafka pentru a fi preluate ulterior.

Aceasta este doar o implementare simplă pentru a ilustra ideea de bază. Într-un scenariu real, veți dori să gestionați mai multe aspecte, cum ar fi gestionarea erorilor, scalabilitatea, menținerea stării consumatorului etc. De asemenea, asigurați-vă că aveți o instanță de Kafka server pornită local pentru a putea testa acest exemplu.


Bineînțeles! Dacă doriți ca producătorul să pună toate mesajele pe Kafka Topic, iar consumatorul să gestioneze doar câte 10 mesaje odată, puteți face următoarele modificări în exemplul anterior:

ProducerService - producătorul de mesaje:

```java
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class ProducerService {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "mesaje";

        for (int i = 0; i < 100; i++) {
            String message = "Mesajul " + i;
            producer.send(new ProducerRecord<>(topic, message), (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Mesajul trimis: " + message);
                } else {
                    System.err.println("Eroare la trimiterea mesajului: " + exception.getMessage());
                }
            });
        }

        producer.close();
    }
}
```

ConsumerService - consumatorul de mesaje:

```java
import org.apache.kafka.clients.consumer.*;
import java.util.Collections;
import java.util.Properties;

public class ConsumerService {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "grup-consumatori");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        String topic = "mesaje";

        consumer.subscribe(Collections.singletonList(topic));

        int mesajeDeConsumat = 10;
        int totalMesajeConsumate = 0;

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);

            if (records.isEmpty()) {
                continue;
            }

            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Mesaj consumat: " + record.value());
                totalMesajeConsumate++;

                if (totalMesajeConsumate >= mesajeDeConsumat) {
                    break;
                }
            }

            if (totalMesajeConsumate >= mesajeDeConsumat) {
                break;
            }
        }

        consumer.close();
    }
}
```

În acest exemplu, `ProducerService` va pune 100 de mesaje pe Kafka Topicul "mesaje". Apoi, `ConsumerService` va prelua doar primele 10 mesaje disponibile din acest topic și le va afișa. După ce a preluat cele 10 mesaje, consumatorul se va închide.

Rețineți că, în scenariul real, ar trebui să gestionați mai bine evenimentele de la producător la consumator, să vă asigurați că consumatorul se încarcă din nou când sunt disponibile mai multe mesaje și să aveți o strategie adecvată de gestionare a erorilor și a stării consumatorului.

Dacă doriți ca mesajele să fie reprocesate în cazul în care apar erori după ce au fost consumate, puteți urma o abordare numită "dead-letter queue" sau "topic de eroare". Această abordare implică utilizarea a două topicuri Kafka - unul pentru mesajele principale și unul pentru mesajele care au eșuat în timpul procesării.

Vom face următoarele modificări pentru a implementa această abordare:

ProducerService - producătorul de mesaje:

```java
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class ProducerService {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "mesaje";

        for (int i = 0; i < 100; i++) {
            String message = "Mesajul " + i;
            producer.send(new ProducerRecord<>(topic, message), (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Mesajul trimis: " + message);
                } else {
                    System.err.println("Eroare la trimiterea mesajului: " + exception.getMessage());

                    // În cazul unei erori, trimitem mesajul în topicul de eroare
                    producer.send(new ProducerRecord<>("mesaje-eroare", message), (errorMetadata, errorException) -> {
                        if (errorException == null) {
                            System.out.println("Mesajul de eroare trimis: " + message);
                        } else {
                            System.err.println("Eroare la trimiterea mesajului de eroare: " + errorException.getMessage());
                        }
                    });
                }
            });
        }

        producer.close();
    }
}
```

ConsumerService - consumatorul de mesaje:

```java
import org.apache.kafka.clients.consumer.*;
import java.util.Collections;
import java.util.Properties;

public class ConsumerService {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "grup-consumatori");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        String topic = "mesaje";

        consumer.subscribe(Collections.singletonList(topic));

        int mesajeDeConsumat = 10;
        int totalMesajeConsumate = 0;

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);

            if (records.isEmpty()) {
                continue;
            }

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

        consumer.close();
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
```

În acest exemplu, dacă procesarea unui mesaj eșuează din cauza unei excepții, mesajul va fi trimis în topicul "mesaje-eroare". Acolo, puteți avea un alt consumator care să se ocupe de re-procesarea acestor mesaje cu erori și să încerce să le prelucreze până când reușesc.

De asemenea, rețineți că într-un scenariu real, ar trebui să tratați mai bine gestionarea erorilor și să asigurați că mesajele cu erori sunt re-procesate în mod corespunzător pentru a nu intra în bucle nesfârșite de reprocesare.


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


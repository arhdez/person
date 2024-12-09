package example.person.config;

import example.person.dto.AddressKafkaDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    // Define the consumer configuration properties
    private Map<String, Object> consumerConfig() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "person-api-group");

        // Set StringDeserializer for key
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Set JsonDeserializer for value and specify the trusted package and value type
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "example.person.dto"); // Specify trusted package for deserialization
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "example.person.dto.AddressKafkaDto"); // Fully qualified class name for AddressKafkaDto

        return properties;
    }

    // Consumer factory setup
    @Bean
    public ConsumerFactory<String, AddressKafkaDto> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    // KafkaListenerContainerFactory setup
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AddressKafkaDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AddressKafkaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // Configure error handler
        /*DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new FixedBackOff(1000L, 3) // Retry every 1 second for up to 3 retries
        );

        // Set error handler to the factory
        factory.setErrorHandler(errorHandler);*/

        return factory;
    }
}

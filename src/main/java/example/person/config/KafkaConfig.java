package example.person.config;

import example.person.dto.AddressKafkaDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    // Define the consumer configuration properties
    private Map<String, Object> consumerConfig() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "person-api-group");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return properties;
    }

    // Consumer factory setup
    @Bean
    public ConsumerFactory<String, AddressKafkaDto> consumerFactory() {
        JsonDeserializer<AddressKafkaDto> deserializer = new JsonDeserializer<>(AddressKafkaDto.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(true);

        ErrorHandlingDeserializer<AddressKafkaDto> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), errorHandlingDeserializer);
    }

    // KafkaListenerContainerFactory setup
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AddressKafkaDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AddressKafkaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // Configure error handler to limit retries
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((record, exception) -> System.err.println("Retries exhausted for record: " + record),
                new FixedBackOff(1000L, 3)
                // Backoff time
        );

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}

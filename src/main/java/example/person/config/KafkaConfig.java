package example.person.config;

import example.person.dto.AddressDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Change this to your broker's address
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "person-api-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "example.addressrepo.dto"); // Add the trusted package
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, AddressDto.class.getName()); // Specify the target type for deserialization
        return properties;
    }

    // Consumer factory setup
    @Bean
    public ConsumerFactory<String, AddressDto> consumerFactory() {
        JsonDeserializer<AddressDto> jsonDeserializer = new JsonDeserializer<>(AddressDto.class);
        jsonDeserializer.addTrustedPackages("example.addressrepo.dto"); // Trust the AddressDto package for deserialization
        jsonDeserializer.setUseTypeMapperForKey(false); // Avoid using type mapping for keys

        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), jsonDeserializer);
    }

    // KafkaListenerContainerFactory with error handling
    /*@Bean
    public ConcurrentMessageListenerContainerFactory<String, AddressDto> kafkaListenerContainerFactory() {
        ConcurrentMessageListenerContainerFactory<String, AddressDto> factory = new ConcurrentMessageListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // Optional: Configure error handler (e.g., retries, dead letter policies)
        factory.getContainerProperties().setErrorHandler(new DefaultErrorHandler(
                new FixedBackOff(1000L, 3) // Retry 3 times with 1-second intervals
        ));

        return factory;
    }*/
}

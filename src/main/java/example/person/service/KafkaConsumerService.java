package example.person.service;

import example.person.dto.AddressKafkaDto;
import example.person.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class KafkaConsumerService {

    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);


    @KafkaListener(topics = "address-topic", groupId = "person-api-group")
    public void consumerAddressDto(AddressKafkaDto addressKafkaDto) {
        LOGGER.info("Received payload {}", addressKafkaDto.toString());
        addressService.processAddress(addressKafkaDto);
    }
}

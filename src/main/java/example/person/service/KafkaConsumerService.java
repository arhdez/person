package example.person.service;

import example.person.dto.AddressDto;
import example.person.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class KafkaConsumerService {

    private final AddressRepository addressRepository;
    private final AddressService addressService;


    @KafkaListener(topics = "address-topic", groupId = "person-api-group")
    public void consumerAddressDto(AddressDto addressDto) {
        addressService.processAddress(addressDto);
    }
}

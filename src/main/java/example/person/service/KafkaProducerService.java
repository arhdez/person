package example.person.service;

import example.person.dto.AddressKafkaDto;
import example.person.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, AddressKafkaDto> kafkaTemplate;
    //private final

    public void sendMessage(PersonDto personDto) {

    }
}

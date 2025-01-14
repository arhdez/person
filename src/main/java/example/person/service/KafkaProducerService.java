package example.person.service;

import example.person.dto.PersonAddressDto;
import example.person.dto.PersonAddressKafkaDto;
import example.person.jpa.Address;
import example.person.jpa.Person;
import example.person.repository.AddressRepository;
import example.person.repository.PersonAddressRepository;
import example.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, PersonAddressKafkaDto> kafkaTemplate;
    private final PersonAddressRepository personAddressRepository;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;


    public void sendMessage(PersonAddressDto personDto) {
        Message<PersonAddressKafkaDto> message = MessageBuilder
                .withPayload(createPersonAddressKafkaDto(personDto))
                .setHeader(KafkaHeaders.TOPIC, "person-address-topic")
                .build();
        kafkaTemplate.send(message);
    }
    private PersonAddressKafkaDto createPersonAddressKafkaDto(PersonAddressDto personAddressDto){
        UUID personId = personAddressDto.getPersonId();
        Person person = getPerson(personId);
        List<String> addressesList = getAddressesList(addressesIds(personId));

        return new PersonAddressKafkaDto(personId,
                person.getFirstName(),
                person.getLastName(),
                formatDateOfBirth(person.getDateOfBirth()),
                person.getEmail(),
                addressesList);
    }
    private Person getPerson(UUID personId) {
        return personRepository.findById(personId).orElseThrow(() ->
                new IllegalArgumentException("Person not found with ID: " + personId));
    }
    private List<UUID> addressesIds(UUID personId) {
        return personAddressRepository.findByIdPersonId(personId).stream()
                .map(address -> address.getId().getAddressId())  // Extract the addressId
                .collect(Collectors.toList());  // Collect into a List<UUID>
    }
    private List<String> getAddressesList(List<UUID> addressesIds) {
        if (addressesIds == null || addressesIds.isEmpty()) {
            return Collections.emptyList();  // Return an empty list if the input is null or empty
        }

        return StreamSupport.stream(addressRepository.findAllById(addressesIds).spliterator(), false)
                .map(this::formatAddress)  // Ensure null safety
                .collect(Collectors.toList());
    }
    private String formatAddress(Address address){
        if (address == null){
            return "";
        }
        return  String.join(address.getStreet(), address.getCity(), address.getState(), address.getZipCode());
    }
    private String formatDateOfBirth(LocalDate dateOfBirth){
        if (dateOfBirth == null){
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateOfBirth.format(formatter);
    }
}

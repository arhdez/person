package example.person.service;

import example.person.dto.PersonAddressDto;
import example.person.jpa.PersonAddress;
import example.person.mapper.PersonAddressMapper;
import example.person.repository.AddressRepository;
import example.person.repository.PersonAddressRepository;
import example.person.repository.PersonRepository;
import example.person.validation.DuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PersonAddressService {
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final PersonAddressRepository personAddressRepository;
    private final PersonAddressMapper personAddressMapper;
    private final KafkaProducerService kafkaProducerService;

    public PersonAddressDto createPersonAddress(PersonAddressDto personAddressDto){
        checkDoublePersonAddress(personAddressDto);
        PersonAddressDto createdPersonAddressDto = personAddressMapper.personAddressToPersonAddressDto(personAddressRepository.save(personAddressMapper.personAddressDtoToPersonAddress(personAddressDto)));
        kafkaProducerService.sendMessage(createdPersonAddressDto);
        return createdPersonAddressDto;
    }

    public List<PersonAddressDto> findAll(Pageable pageable){
        PageRequest pageRequest =
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id")));
        return personAddressRepository.findAll(pageRequest).stream().map(personAddressMapper::personAddressToPersonAddressDto).collect(Collectors.toList()) ;
    }
    public List<PersonAddressDto> findAll(){
        Iterable<PersonAddress> personAddresses = personAddressRepository.findAll();
        return StreamSupport.stream(personAddresses.spliterator(), false)
                .map(personAddressMapper::personAddressToPersonAddressDto)
                .collect(Collectors.toList());
    }

    private void checkDoublePersonAddress(PersonAddressDto personAddressDto) {
        checkDoublePersonAddress(personAddressDto.getPersonId(), personAddressDto.getAddressId());
    }
    private void checkDoublePersonAddress(UUID personId, UUID addressId) {
        if (personAddressRepository.existsByIdPersonIdAndIdAddressId(personId, addressId)){
            String fullName = personRepository.findById(personId)
                    .map(person -> person.getFirstName() + " " + person.getLastName())
                    .orElse("Unknown Person");
            String addressDetails = addressRepository.findById(addressId)
                    .map(address -> address.getStreet() + ", "
                            + address.getCity() + ", "
                            + address.getState() + ", "
                            + address.getZipCode())
                    .orElse("Unknown Address");
            throw new DuplicateException("Person: "
                    + fullName + " with Address: " + addressDetails+ " already exists." );
        }
    }
}

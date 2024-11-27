package example.person.service;

import example.person.dto.PersonAddressDto;
import example.person.jpa.Person;
import example.person.mapper.PersonAddressMapper;
import example.person.repository.PersonAddressRepository;
import example.person.repository.PersonRepository;
import example.person.validation.DuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonAddressService {
    private final PersonRepository personRepository;
    private final PersonAddressRepository personAddressRepository;
    private final PersonAddressMapper personAddressMapper;

    public PersonAddressDto createPersonAddress(PersonAddressDto personAddressDto){
        checkDoublePersonAddress(personAddressDto);
        return personAddressMapper.personAddressToPersonAddressDto(personAddressRepository.save(personAddressMapper.personAddressDtoToPersonAddress(personAddressDto)));
    }

    private void checkDoublePersonAddress(PersonAddressDto personAddressDto) {
        checkDoublePersonAddress(personAddressDto.getPersonId(), personAddressDto.getAddressId(),
                personAddressDto.getStreet(), personAddressDto.getCity(),
                personAddressDto.getState(), personAddressDto.getZipCode());
    }
    private void checkDoublePersonAddress(UUID personId, UUID addressId, String street, String city, String state, Integer zipCode) {
        if (personAddressRepository.existsByIdPersonIdAndIdAddressId(personId, addressId)){
            String fullName = personRepository.findById(personId)
                    .map(person -> person.getFirstName() + " " + person.getLastName())
                    .orElse("Unknown");
            throw new DuplicateException("Person: "
                    + fullName + " with Address: " + street + " " + city + " " + state + " " + zipCode + " already exists." );
        }
    }
}

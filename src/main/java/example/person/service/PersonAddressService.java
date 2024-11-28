package example.person.service;

import example.person.dto.PersonAddressDto;
import example.person.dto.PersonDto;
import example.person.dto.PersonWithPersonAddressDto;
import example.person.mapper.PersonAddressMapper;
import example.person.mapper.PersonWithPersonAddressMapper;
import example.person.repository.PersonAddressRepository;
import example.person.repository.PersonRepository;
import example.person.validation.DoesNotExistsException;
import example.person.validation.DuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonAddressService {
    private final PersonRepository personRepository;
    private final PersonAddressRepository personAddressRepository;
    private final PersonAddressMapper personAddressMapper;
    private final PersonWithPersonAddressMapper personWithPersonAddressMapper;
    private final PersonService personService;

    public PersonWithPersonAddressDto createPersonAddress(PersonAddressDto personAddressDto){
        checkDoublePersonAddress(personAddressDto);
        PersonDto personDto = getValidPerson(personAddressDto);
        PersonAddressDto createdPersonAddressDto = personAddressMapper.personAddressToPersonAddressDto(personAddressRepository.save(personAddressMapper.personAddressDtoToPersonAddress(personAddressDto)));
            return personWithPersonAddressMapper.toPersonWithPersonAddressDto(personDto, createdPersonAddressDto);
        //return personAddressMapper.personAddressToPersonAddressDto(personAddressRepository.save(personAddressMapper.personAddressDtoToPersonAddress(personAddressDto)));
    }

    public List<PersonWithPersonAddressDto> findAllPersonWithPersonAddress(Pageable pageable){
        PageRequest pageRequest =
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "street")));
        return personAddressRepository.findAll(pageRequest).stream()
                .map(personAddress -> {
                    PersonAddressDto personAddressDto = personAddressMapper.personAddressToPersonAddressDto(personAddress);
                    PersonDto personDto = getValidPerson(personAddressDto);
                    return personWithPersonAddressMapper.toPersonWithPersonAddressDto(personDto, personAddressDto);
                }).collect(Collectors.toList());
    }
    /*public List<PersonAddressDto> findAllPersonWithPersonAddress(Pageable pageable){
        PageRequest pageRequest =
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "firstName")));
        return personAddressRepository.findAll(pageRequest).stream().map(personAddressMapper::personAddressToPersonAddressDto).collect(Collectors.toList())
    }*/

    private PersonDto getValidPerson(PersonAddressDto personAddress) {
        Optional<PersonDto> personDto = personService.findById(personAddress.getPersonId());
        if(personDto.isEmpty()) {
            throw new DoesNotExistsException("The Person does not exist");
        }
        return personDto.get();
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

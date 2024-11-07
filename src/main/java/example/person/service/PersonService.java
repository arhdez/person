package example.person.service;

import example.person.mapper.PersonMapper;
import example.person.dto.PersonDto;
import example.person.jpa.Person;
import example.person.mapper.PersonMapperPatch;
import example.person.repository.PersonRepository;
import example.person.validation.DuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    private PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public PersonDto createPerson(PersonDto personDto) {
        if (personRepository.existsByEmail(personDto.getEmail())){
            throw new DuplicateException("Email already exists: " + personDto.getEmail());
        }
        // Save person and get the generated ID
        return personMapper.personToPersonDto(personRepository.save(personMapper.personDtoToPerson(personDto)));
    }

    public Optional<PersonDto> updatePerson(PersonDto personToUpdateDto, UUID requestedId) {
        Optional<Person> existentPersonOptional = personRepository.findById(requestedId);
        Optional<Person> findEmail = personRepository.findByEmail(personToUpdateDto.getEmail());

        if (findEmail.isPresent() && findEmail.get().getId() != requestedId){
            throw new DuplicateException("Email already exists: " + personToUpdateDto.getEmail());
        }
        if (existentPersonOptional.isPresent()) {
            Person existentPerson = existentPersonOptional.get();
            personMapper.update(existentPerson, personToUpdateDto);
            return Optional.of(personMapper.personToPersonDto(personRepository.save(existentPerson)));
        }
        return Optional.empty();
    }

    @Autowired
    private PersonMapperPatch personMapperPatch;

    public Optional<PersonDto> updatePersonByFields(UUID requestedId, PersonDto fieldsDto){
        Optional<Person> existingPersonOptional = personRepository.findById(requestedId);
        if (existingPersonOptional.isPresent()) {
            Person existingPerson = existingPersonOptional.get();
            personMapperPatch.updatePersonFromDto(fieldsDto, existingPerson);
            return Optional.of(personMapper.personToPersonDto(personRepository.save(existingPerson)));
        }
        return Optional.empty();
    }

    public Optional<PersonDto> findById(UUID requestedId) {
        Person existentPerson = personRepository.findById(requestedId).orElse(null);
        return Optional.of(personMapper.personToPersonDto(existentPerson));
    }

    public List<PersonDto> findByFirstName(String requestedFirstName, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "lastName")));
        Page<Person> personPage = personRepository.findByFirstName(requestedFirstName, pageRequest);
        return personPage.getContent().stream().map(personMapper::personToPersonDto).collect(Collectors.toList());
    }

    public List<PersonDto> findAll(Pageable pageable) {
        PageRequest pageRequest =
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "firstName")));
        return personRepository.findAll(pageRequest).stream().map(personMapper::personToPersonDto).collect(Collectors.toList());
    }

    public void delete(UUID requestedId) {
        personRepository.deleteById(requestedId);
    }
}

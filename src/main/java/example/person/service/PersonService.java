package example.person.service;

import example.person.dto.PersonDto;
import example.person.jpa.Person;
import example.person.mapper.PersonMapper;
import example.person.repository.PersonRepository;
import example.person.validation.DuplicateException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;


    public PersonDto createPerson(PersonDto personDto) {
        if (personRepository.existsByEmail(personDto.getEmail())) {
            throw new DuplicateException("Email already exists: " + personDto.getEmail());
        }
        // Save person and get the generated ID
        return personMapper.personToPersonDto(personRepository.save(personMapper.personDtoToPerson(personDto)));
    }

    public Optional<PersonDto> updatePerson(PersonDto personToUpdateDto, UUID requestedId) {
        Optional<Person> existentPersonOptional = personRepository.findById(requestedId);
        nonDuplicatedPerson(personToUpdateDto, requestedId);
        if (existentPersonOptional.isPresent()) {
            Person existentPerson = existentPersonOptional.get();
            personMapper.update(existentPerson, personToUpdateDto);
            return Optional.of(personMapper.personToPersonDto(personRepository.save(existentPerson)));
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

    private void nonDuplicatedPerson(PersonDto personToUpdateDto, UUID requestedId) {
        Optional<Person> findEmail = personRepository.findByEmail(personToUpdateDto.getEmail());

        if (findEmail.isPresent() && findEmail.get().getId() != requestedId) {
            throw new DuplicateException("Email already exists: " + personToUpdateDto.getEmail());
        }
    }
}

package example.person;

import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/persons")
@Import(SecurityConfig.class)//Import security bypass
public class PersonController {
    private final PersonRepository personRepository;

    private PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/id/{requestedId}")
    private ResponseEntity<Person> findById(@PathVariable UUID requestedId) {
        Optional<Person> person = personRepository.findById(requestedId);
        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{requestedFirstName}")
    private ResponseEntity<List<Person>> findByFirstName(@PathVariable String requestedFirstName, Pageable pageable) {
        Page<Person> page = personRepository.findByFirstName(requestedFirstName,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "lastName"))
                )
        );
        return ResponseEntity.ok(page.getContent());
    }

   @GetMapping
    private ResponseEntity<List<Person>> findAll(Pageable pageable) {
        Page<Person> page = personRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "firstName"))
                )
        );
        return ResponseEntity.ok(page.getContent());
    }

    @PostMapping
    private ResponseEntity<Void> createPerson(@RequestBody Person newPersonRequest, UriComponentsBuilder ucb) {
        Person person = new Person(null, newPersonRequest.getFirstName(), newPersonRequest.getLastName());
        Person savedPerson = personRepository.save(person);
        URI locationOfNewPerson = ucb
                .path("persons/{id}")
                .buildAndExpand(savedPerson.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewPerson).build();
    }

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putPerson(@PathVariable UUID requestedId, @RequestBody Person personUpdate) {
        Optional<Person> person = personRepository.findById(requestedId);
        if (person.isPresent()) {
            Person updatedPerson = new Person(personUpdate.getId(), personUpdate.getFirstName(), personUpdate.getLastName());
            personRepository.save(updatedPerson);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{requestedId}")
    private ResponseEntity<Void> patchPerson(@PathVariable UUID requestedId,
                                             @RequestBody Map<String, Object> personUpdate) {
        Optional<Person> optionalPerson = personRepository.findById(requestedId);
        if (optionalPerson.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Person person = optionalPerson.get();

        personUpdate.forEach((key, value)->{
            switch (key){
                case "firstName" -> person.setFirstName((String) value);
                case "lastName" -> person.setLastName((String) value);
            }
        });
        Person updatedPerson = personRepository.save(person);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deletePerson(@PathVariable UUID id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

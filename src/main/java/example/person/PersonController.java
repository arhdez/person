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
import java.util.*;

@RestController
@RequestMapping("/persons")
@Import(SecurityConfig.class)//Import security bypass
public class PersonController {
    private final PersonRepository personRepository;
    private  final SSNEncryptionService ssnEncryptionService;

    private PersonController(PersonRepository personRepository, SSNEncryptionService ssnEncryptionService) {
        this.personRepository = personRepository;
        this.ssnEncryptionService = ssnEncryptionService;
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

    @GetMapping("/firstname/{requestedFirstName}")
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
    public ResponseEntity<Void> createPerson(@RequestBody Person newPersonRequest, UriComponentsBuilder ucb) {
        // Construct new Person object
        Person person = new Person(null, newPersonRequest.getFirstName(), newPersonRequest.getLastName(), null);

        // Encrypt the SSN and set it on the Person object

        person.setSsn(ssnEncryptionService.encrypt(Arrays.toString(newPersonRequest.getSsn()))); // Encrypt directly without converting to string
        // Save person and get the generated ID
        Person savedPerson = personRepository.save(person);

        // Create URI location for the new resource
        URI locationOfNewPerson = ucb
                .path("persons/{id}")
                .buildAndExpand(savedPerson.getId())
                .toUri();

        // Return the response with 201 status and Location header
        return ResponseEntity.created(locationOfNewPerson).build();
    }

    /*@PostMapping
    private ResponseEntity<Void> createPerson(@RequestBody Person newPersonRequest, UriComponentsBuilder ucb) {
        Person person = new Person(null, newPersonRequest.getFirstName(), newPersonRequest.getLastName(),
                newPersonRequest.getSsn());
        person.setSsn(ssnEncryptionService.encrypt(Arrays.toString(newPersonRequest.getSsn())));
        Person savedPerson = personRepository.save(person);
        URI locationOfNewPerson = ucb
                .path("persons/{id}")
                .buildAndExpand(savedPerson.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewPerson).build();
    }*/

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putPerson(@PathVariable UUID requestedId, @RequestBody Person personUpdate) {
        Optional<Person> person = personRepository.findById(requestedId);
        if (person.isPresent()) {
            Person updatedPerson = new Person(personUpdate.getId(), personUpdate.getFirstName(), personUpdate.getLastName(), personUpdate.getSsn());
            personRepository.save(updatedPerson);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
   /* @PutMapping("/{requestedId}"){
        private
    }*/
    /*@PatchMapping("/{requestedId}")
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
    }*/

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deletePerson(@PathVariable UUID id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

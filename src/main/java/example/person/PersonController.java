package example.person;

import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
@Import(SecurityConfig.class)//Import security bypass
public class PersonController {
    private final PersonRepository personRepository;

    private PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /*@GetMapping("/{requestedId}")//it's for testing
    private ResponseEntity<Person> findById(@PathVariable Long requestedId){
        if (requestedId.toString().equals("99")) {
            Person person = new Person(99L, "John", "Doe");
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
    @GetMapping("/{requestedId}")
    private ResponseEntity<Person> findById(@PathVariable Long requestedId) {
        Optional<Person> person = personRepository.findById(requestedId);
        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    private ResponseEntity<Iterable<Person>> findAll() {
        return ResponseEntity.ok(personRepository.findAll());
    }

    @PostMapping
    private ResponseEntity<Void> createPerson(@RequestBody Person newPersonRequest, UriComponentsBuilder ucb) {
        Person person = new Person(null, newPersonRequest.getFirstName(), newPersonRequest.getLastName());
        Person savedPerson = personRepository.save(person);
        URI locationOfNewPerson = ucb
                .path("person/{id}")
                .buildAndExpand(savedPerson.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewPerson).build();
    }
}

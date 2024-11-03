package example.person.controller;

import example.person.config.SecurityConfig;
import example.person.dto.PersonDto;
import example.person.service.PersonService;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/persons")
@Import(SecurityConfig.class)//Import security bypass
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {this.personService = personService;}

    @GetMapping("/id/{requestedId}")
    public ResponseEntity<PersonDto> findById(@PathVariable UUID requestedId) {
        Optional<PersonDto> person = personService.findById(requestedId);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/firstname/{requestedFirstName}")
    public ResponseEntity<List<PersonDto>> findByFirstName(@PathVariable String requestedFirstName, Pageable pageable) {
        return ResponseEntity.ok(personService.findByFirstName(requestedFirstName, pageable));
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto newPersonRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.createPerson(newPersonRequest));
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<PersonDto> putPerson(@PathVariable UUID requestedId, @RequestBody PersonDto personUpdate) {
        Optional<PersonDto> person = personService.updatePerson(personUpdate, requestedId);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
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
}
